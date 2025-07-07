package user_service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
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
	void isEmailAvailable_EmailAvailable_ReturnsTrue() {
		String email = "aaaaaa@gmail.com";
		Mockito.when(userRepository.findByEmail(email))
				.thenReturn(Optional.empty());

		boolean isAvailable = userService.isEmailAvailable(email);
		Assertions.assertTrue(isAvailable);
	}

	@Test
	void isEmailAvailable_EmailNotAvailable_ReturnsFalse() {
		String email = "aaaaaa@gmail.com";
		User user = new User();
		user.setEmail(email);

		Mockito.when(userRepository.findByEmail(email))
				.thenReturn(Optional.of(user));

		boolean isAvailable = userService.isEmailAvailable(email);
		Assertions.assertFalse(isAvailable);
	}

	@Test
	void getUserByEmail_UserNotFound_ReturnsEmptyOptional() {
		String email = "aaaaaa@gmail.com";
		Mockito.when(userRepository.findByEmail(email))
				.thenReturn(Optional.empty());

		Optional<User> user = userService.getUserByEmail(email);
		Assertions.assertTrue(user.isEmpty());
	}

	@Test
	void getUserByEmail_UserNotFound_ReturnsUserOptional() {
		String email = "aaaaaa@gmail.com";
		User user = new User();
		user.setEmail(email);
		Mockito.when(userRepository.findByEmail(email))
				.thenReturn(Optional.of(user));

		Optional<User> userFound = userService.getUserByEmail(email);
        Assertions.assertEquals(userFound.get().getEmail(), email);
	}

	@Test
	void contextLoads() {
	}

}
