package pl.springrest.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
		user = new User(1L, "fn", "ln", "login", "pass", "user@email.com");
	}
	
	@After
	public void afterMethod() {
		userRepository.flush();
	}
	
	@Test
	public void findUserByLogin() {
		userRepository.save(user);
		User foundUser = userRepository.findByLogin(user.getLogin());
		assertEquals(user, foundUser);
	}
	
	@Test
	public void ifUserNotFoundShouldReturnNull() {
		User foundUser = userRepository.findByLogin(user.getLogin());
		assertEquals(null, foundUser);
	}
	
	@Test
	public void updatedAddressNotNull() {
		Address address = new Address("Poland", "Warsaw", "Str", "00-000");
		User foundUser = userRepository.findOne(user.getId());
		foundUser.setAddress(address);
		User user2 = userRepository.findOne(user.getId());
		assertNotNull(user2.getAddress());
		assertThat(user2.getAddress()).isEqualTo(address);
	}
	
	@Test
	public void deleteUserFromDB() {
		userRepository.save(user);
		userRepository.delete(user);
		User foundUser = userRepository.findByLogin(user.getLogin());
		assertEquals(null, foundUser);
	}
}
