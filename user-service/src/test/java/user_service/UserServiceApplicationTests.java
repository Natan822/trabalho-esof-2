package user_service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import user_service.model.User;
import user_service.repository.UserRepository;
import user_service.service.UserService;

import java.util.Optional;

@SpringBootTest
class UserServiceApplicationTests {

	@Mock
	UserRepository userRepository;
	@InjectMocks
	UserService userService;

	@Test
	void isEmailAvailable_EmailAvailable_True() {
		String email = "aaaaaa@gmail.com";
		Mockito.when(userRepository.findByEmail(email))
				.thenReturn(Optional.empty());

		boolean isAvailable = userService.isEmailAvailable(email);
		Assertions.assertTrue(isAvailable);
	}

	@Test
	void isEmailAvailable_EmailNotAvailable_False() {
		String email = "aaaaaa@gmail.com";
		User user = new User();
		user.setEmail(email);

		Mockito.when(userRepository.findByEmail(email))
				.thenReturn(Optional.of(user));

		boolean isAvailable = userService.isEmailAvailable(email);
		Assertions.assertFalse(isAvailable);
	}

	@Test
	void contextLoads() {
	}

}
