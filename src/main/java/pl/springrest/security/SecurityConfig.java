package pl.springrest.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String ROLE_USER = "USER";
	private static final String ROLE_ADMIN = "ADMIN";

	private UserDetailsService userDetailsService;
	
	public SecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("user")
				.password("password")
				.roles(ROLE_USER)
			.and()
				.withUser("admin")
				.password("admin")
				.roles(ROLE_USER, ROLE_ADMIN);
		
		//auth.userDetailsService(userDetailsService);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
			http
				.authorizeRequests()
					.antMatchers(HttpMethod.POST ,"/films", "/actors").hasRole(ROLE_ADMIN)
					.antMatchers(HttpMethod.GET, "/users").hasRole(ROLE_ADMIN)
					.anyRequest().permitAll()
				.and()
					.httpBasic()
				.and()
					.csrf().disable();
			
	}
	
}
