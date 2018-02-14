package pl.springrest.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//TODO write better password encoder
@SuppressWarnings("deprecation")
@Configuration
public class PasswordEncoderConfig {

	@Bean
	public static PasswordEncoder passwordEncoder() {
	    return NoOpPasswordEncoder.getInstance();
	}
}
