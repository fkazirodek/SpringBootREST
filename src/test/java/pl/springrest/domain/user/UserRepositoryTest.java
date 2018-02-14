package pl.springrest.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

	private static final String FIRST_NAME = "Tom";
	private static final String LAST_NAME = "Cruise";
	private static final String LOGIN = "user";
	private static final String PASSWORD = "password";
	private static final String EMAIL = "user@email.com";

	@Autowired
	private UserRepository userRepository;
	
	private User user;
	
	@Before
	public void setUp() {
		user = new User(FIRST_NAME, LAST_NAME, LOGIN, PASSWORD, EMAIL);
	}
	
	@After
	public void clean() {
		userRepository.deleteAll();
	}
	
	@Test
	public void findUserByLoginReturnUser() {
		userRepository.save(user);
		
		User foundUser = userRepository.findByLogin(LOGIN).get();
		
		assertEquals(user, foundUser);
	}
	
	@Test
	public void ifUserNotFoundShouldReturnEmptyOptional() {
		Optional<User> foundUser = userRepository.findByLogin(LOGIN);
		
		assertFalse(foundUser.isPresent());
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void ifDuplicateUserThrowsDataIntegrityViolationEx() {
		User duplicateUser = new User(FIRST_NAME, LAST_NAME, LOGIN, PASSWORD, EMAIL);
		userRepository.save(user);
		userRepository.save(duplicateUser);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void ifUserIsNotValidThrowsDataIntegrityViolationEx() {
		User invalidUser = new User(FIRST_NAME, LAST_NAME, null, PASSWORD, EMAIL);
		userRepository.save(invalidUser);
	}
	
	@Test
	@Commit
	@Transactional
	public void updateAddressReturnUserWithUpdatedAddress() {
		Address address = new Address("Poland", "Warsaw", "Str", "00-000");
		
		User savedUser = userRepository.save(user);
		savedUser.setAddress(address);
		User updatedUser = userRepository.findByLogin(LOGIN).get();
		
		assertNotNull(updatedUser.getAddress());
		assertThat(updatedUser.getAddress()).isEqualTo(address);
	}
	
	@Test
	public void deleteUserFromDB() {
		userRepository.save(user);
		
		userRepository.delete(user);
		Optional<User> foundUser = userRepository.findByLogin(LOGIN);
		
		assertFalse(foundUser.isPresent());
	}
}
