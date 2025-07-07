package auth_service.service;

import auth_service.dto.LoginDTO;
import auth_service.dto.RegisterDTO;
import auth_service.model.UserCredentials;
import auth_service.repository.AuthRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final String userServiceEndpoint = "http://user-service/api/users";

    // Registra usuário no BD de usuários
    private Mono<ResponseEntity<String>> addUser(RegisterDTO registerDTO) {
        return webClientBuilder.build()
                .post()
                .uri(userServiceEndpoint + "/add")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(
                        BodyInserters
                        .fromFormData("email", registerDTO.email())
                        .with("firstName", registerDTO.firstName())
                        .with("lastName", registerDTO.lastName())
                )
                .retrieve()
                .onStatus(HttpStatus.CONFLICT::equals, response -> {
                    return Mono.empty();
                })
                .toEntity(String.class);
    }

    // Adiciona usuário no BD de credenciais
    private void addCredentials(UserCredentials credentials) {
        credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
        authRepository.save(credentials);
    }

    // Extrai o ID de usuário de um resposta do user-service
    private Long getIdFromResponse(ResponseEntity<String> response) {
        Map<String, Object> bodyParsed;
        Map<String, Object> userParsed;
        try {
            ObjectMapper mapper = new ObjectMapper();
            bodyParsed = mapper.readValue(response.getBody(), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        userParsed = (Map<String, Object>) bodyParsed.get("user");
        return Long.valueOf((Integer) userParsed.get("id"));
    }

    public Mono<ResponseEntity<?>> register(RegisterDTO registerDTO) {
        return addUser(registerDTO)
                .flatMap(response -> {
                    if (response.getStatusCode().value() == HttpStatus.OK.value()) {
                        Long userId = getIdFromResponse(response);
                        addCredentials(new UserCredentials(userId, registerDTO.password()));
                    }
                    return Mono.just(response);
                });
    }

    private Optional<UserCredentials> getCredentialById(Long id) {
        return authRepository.findById(id);
    }

    private Mono<ResponseEntity<String>> getUserByEmail(String email) {
        return webClientBuilder.build()
                .get()
                .uri(userServiceEndpoint + "/search?email=" + email)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response -> {
                    return Mono.empty();
                })
                .toEntity(String.class);
    }

    public Mono<ResponseEntity<?>> login(LoginDTO loginDTO) {
        return getUserByEmail(loginDTO.email())
                .flatMap(response -> {
                    if (response.getStatusCode().value() == HttpStatus.OK.value()) {
                        Long id = getIdFromResponse(response);
                        Optional<UserCredentials> credentials = getCredentialById(id);
                        if (credentials.isEmpty()) {
                            return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                    .body(Map.of("error", "Usuário não encontrado.")));
                        }

                        // Senha incorreta
                        if (!passwordEncoder.matches(loginDTO.password(), credentials.get().getPassword())) {
                            return Mono.just(ResponseEntity.ok(Map.of("error", "Senha incorreta.")));
                        }
                        else {
                            // TODO: Gerar token de login
                            return Mono.just(ResponseEntity.ok(Map.of("success", "Login realizado.")));
                        }
                    }
                    else {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("error", "Email não cadastrado.")));
                    }
                });
    }

}
