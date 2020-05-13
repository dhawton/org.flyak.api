package org.nzvirtual.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nzvirtual.api.data.entity.User;
import org.nzvirtual.api.data.repository.UserRepository;
import org.nzvirtual.api.dto.UserRequest;
import org.nzvirtual.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
class ApiApplicationTests {
	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	UserService userService;
	private Long testUser;

	@Test
	void contextLoads() {
	}

	@Test
	void userTests() {
		User user = new User();
		user.setFirstname("Test");
		user.setLastname("User");
		user.setEmail("test@flyak.test");
		user.setPassword(passwordEncoder.encode("test1234"));
		userRepository.save(user);
		this.testUser = user.getId();

		Optional<User> user1 = userRepository.findById(this.testUser);
		assertTrue(user1.isPresent());

		UserRequest userRequest = new UserRequest();
		userRequest.setEmail("test2@flyak.test");
		userRequest.setGenpassword(true);
		userRequest.setNewpassword("test1111");
		userRequest.setFirstname("Test");
		userRequest.setLastname("User Changed");

		userService.changeUser(this.testUser, userRequest);

		user1 = userRepository.findById(this.testUser);
		assertTrue(user1.isPresent());
		User userEntity = user1.get();

		Assertions.assertFalse(BCrypt.checkpw("test1234", userEntity.getPassword()));
		Assertions.assertEquals("Test User Changed", userEntity.getName(),"Name is not changed " + userEntity.getName());

		Optional<User> optionalUser = userRepository.findById(this.testUser);
		assertTrue(optionalUser.isPresent());

		user = optionalUser.get();

		userService.changePassword(user, "test54321");

		Assertions.assertTrue(BCrypt.checkpw("test54321", user.getPassword()));

		optionalUser = userRepository.findById(this.testUser);
		assertTrue(optionalUser.isPresent());

		userRepository.delete(optionalUser.get());

		Optional<User> optionalUser1 = userRepository.findById(this.testUser);
		assertTrue(optionalUser1.isEmpty());
	}
}
