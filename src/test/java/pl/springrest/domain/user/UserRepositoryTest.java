package pl.springrest.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	private User user;
	
	@Before
	public void beforeMethod() {
		user = new User("fn", "ln", "login", "password", "user@email.com");
	}
	
	@After
	public void afterMethod() {
		userRepository.deleteAll();
	}
	
	@Test
	public void findUserByLogin() {
		userRepository.save(user);
		Optional<User> foundUser = userRepository.findByLogin(user.getLogin());
		assertEquals(user, foundUser.get());
	}
	
	@Test
	public void ifUserNotFoundShouldReturnNull() {
		Optional<User> foundUser = userRepository.findByLogin(user.getLogin());
		assertNull(foundUser.orElse(null));
	}
	
	@Test
	public void updatedAddressNotNull() {
		Address address = new Address("Poland", "Warsaw", "Str", "00-000");
		User foundUser = userRepository.save(user);
		foundUser.setAddress(address);
		User user2 = userRepository.findOne(1L);
		assertNotNull(user2.getAddress());
		assertThat(user2.getAddress()).isEqualTo(address);
	}
	
	@Test
	public void deleteUserFromDB() {
		userRepository.save(user);
		userRepository.delete(user);
		Optional<User> foundUser = userRepository.findByLogin(user.getLogin());
		assertNull(foundUser.orElse(null));
	}
}
