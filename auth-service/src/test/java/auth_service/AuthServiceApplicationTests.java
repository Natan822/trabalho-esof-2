package auth_service;

import auth_service.dto.LoginDTO;
import auth_service.dto.RegisterDTO;
import auth_service.model.UserCredentials;
import auth_service.repository.AuthRepository;
import auth_service.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceApplicationTests {

	@Mock
	private WebClient.Builder webClientBuilder;

	@Mock
	private AuthRepository authRepository;

	@Mock
	private BCryptPasswordEncoder passwordEncoder;

	@InjectMocks
	private AuthService authService;

	private WebClient webClient;

	private WebClient.RequestBodyUriSpec postBodyUriSpec;
	private WebClient.RequestHeadersSpec<?> postHeadersSpec;
	private WebClient.ResponseSpec postResponseSpec;

	private WebClient.RequestHeadersUriSpec getUriSpec;
	private WebClient.RequestHeadersSpec<?> getHeadersSpec;
	private WebClient.ResponseSpec getResponseSpec;

	@BeforeEach
	void setup() {
		webClient = mock(WebClient.class);

		postBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
		postHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
		postResponseSpec = mock(WebClient.ResponseSpec.class);

		getUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
		getHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
		getResponseSpec = mock(WebClient.ResponseSpec.class);

		when(webClientBuilder.build()).thenReturn(webClient);
	}

	private void stubAddUserResponse(Mono<ResponseEntity<String>> responseMono) {
		when(webClient.post()).thenReturn(postBodyUriSpec);
		when(postBodyUriSpec.uri(anyString())).thenReturn(postBodyUriSpec);
		when(postBodyUriSpec.header(anyString(), anyString())).thenReturn(postBodyUriSpec);
		when(postBodyUriSpec.body(any(BodyInserter.class))).thenReturn(postHeadersSpec);
		when(postHeadersSpec.retrieve()).thenReturn(postResponseSpec);
		when(postResponseSpec.onStatus(any(Predicate.class), any(Function.class)))
				.thenReturn(postResponseSpec);
		when(postResponseSpec.toEntity(String.class)).thenReturn(responseMono);
	}

	private void stubGetUserByEmailResponse(Mono<ResponseEntity<String>> responseMono) {
		when(webClient.get()).thenReturn(getUriSpec);
		when(getUriSpec.uri(anyString())).thenReturn(getHeadersSpec);
		when(getHeadersSpec.retrieve()).thenReturn(getResponseSpec);
		when(getResponseSpec.onStatus(any(Predicate.class), any(Function.class)))
				.thenReturn(getResponseSpec);
		when(getResponseSpec.toEntity(String.class)).thenReturn(responseMono);
	}

	@Test
	void register_whenUserServiceReturnsOk_savesEncodedCredentials() throws Exception {
		var dto = new RegisterDTO("alice@example.com", "Alice", "Liddell", "mypassword");
		var okResponse = ResponseEntity
				.ok("{\"user\":{\"id\":13}}");

		stubAddUserResponse(Mono.just(okResponse));
		when(passwordEncoder.encode("mypassword")).thenReturn("encodedPwd123");

		StepVerifier.create(authService.register(dto))
				.assertNext(resp -> {
					assertEquals(HttpStatus.OK, resp.getStatusCode());

					// Verify that we saved credentials with the right ID and encoded password
					ArgumentCaptor<UserCredentials> captor =
							ArgumentCaptor.forClass(UserCredentials.class);
					verify(authRepository).save(captor.capture());

					UserCredentials saved = captor.getValue();
					assertEquals(13L, saved.getUserId());
					assertEquals("encodedPwd123", saved.getPassword());
				})
				.verifyComplete();
	}

	@Test
	void register_whenUserServiceReturnsConflict_doesNotSaveCredentials() {
		var dto = new RegisterDTO("ronald@example.com", "Ronald", "McDonald", "bigmac123");
		var conflictResponse = ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body("");

		stubAddUserResponse(Mono.just(conflictResponse));

		StepVerifier.create(authService.register(dto))
				.assertNext(resp -> {
					assertEquals(HttpStatus.CONFLICT, resp.getStatusCode());
				})
				.verifyComplete();

		verify(authRepository, never()).save(any());
	}

	@Test
	void login_whenEmailNotRegistered_returns404WithEmailError() {
		var dto = new LoginDTO("nobody@example.com", "aaaaaaaaaa");
		var notFound = ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body("");

		stubGetUserByEmailResponse(Mono.just(notFound));

		StepVerifier.create(authService.login(dto))
				.assertNext(entity -> {
					assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());

					@SuppressWarnings("unchecked")
					Map<String, String> body = (Map<String, String>) entity.getBody();
					assertEquals("Email não cadastrado.", body.get("error"));
				})
				.verifyComplete();
	}

	@Test
	void login_whenCredentialsMissing_returns404UserNotFound() {
		var dto = new LoginDTO("geralt@example.com", "pass123");
		var okResponse = ResponseEntity
				.ok("{\"user\":{\"id\":7}}");

		stubGetUserByEmailResponse(Mono.just(okResponse));
		when(authRepository.findById(7L)).thenReturn(Optional.empty());

		StepVerifier.create(authService.login(dto))
				.assertNext(entity -> {
					assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());

					@SuppressWarnings("unchecked")
					Map<String, String> body = (Map<String, String>) entity.getBody();
					assertEquals("Usuário não encontrado.", body.get("error"));
				})
				.verifyComplete();
	}

	@Test
	void login_whenPasswordIncorrect_returnsOkWithError() {
		var dto = new LoginDTO("dave@example.com", "wrongpass");
		var okResponse = ResponseEntity
				.ok("{\"user\":{\"id\":21}}");

		stubGetUserByEmailResponse(Mono.just(okResponse));
		var storedCreds = new UserCredentials(21L, "encodedSecret");
		when(authRepository.findById(21L)).thenReturn(Optional.of(storedCreds));
		when(passwordEncoder.matches("wrongpass", "encodedSecret")).thenReturn(false);

		StepVerifier.create(authService.login(dto))
				.assertNext(entity -> {
					assertEquals(HttpStatus.OK, entity.getStatusCode());

					@SuppressWarnings("unchecked")
					Map<String, String> body = (Map<String, String>) entity.getBody();
					assertEquals("Senha incorreta.", body.get("error"));
				})
				.verifyComplete();
	}

	@Test
	void login_whenPasswordMatches_returnsOkWithSuccess() {
		var dto = new LoginDTO("eve@example.com", "rightpass");
		var okResponse = ResponseEntity
				.ok("{\"user\":{\"id\":99}}");

		stubGetUserByEmailResponse(Mono.just(okResponse));
		var storedCreds = new UserCredentials(99L, "hashedPwd");
		when(authRepository.findById(99L)).thenReturn(Optional.of(storedCreds));
		when(passwordEncoder.matches("rightpass", "hashedPwd")).thenReturn(true);

		StepVerifier.create(authService.login(dto))
				.assertNext(entity -> {
					assertEquals(HttpStatus.OK, entity.getStatusCode());

					@SuppressWarnings("unchecked")
					Map<String, String> body = (Map<String, String>) entity.getBody();
					assertEquals("Login realizado.", body.get("success"));
				})
				.verifyComplete();
	}
}
