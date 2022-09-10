package com.todo.todo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.todo.todo.services.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
			.authorizeHttpRequests((requests) -> requests
				.antMatchers("/",
				"/index",
				"/registrate",
				"/webjars/**").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin((form) -> form
				.loginPage("/login")
				.permitAll()
			)
			.logout((logout) -> logout.permitAll());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
		.userDetailsService(userService)
		.passwordEncoder(new BCryptPasswordEncoder());
    }

}
