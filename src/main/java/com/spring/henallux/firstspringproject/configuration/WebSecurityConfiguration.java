package com.spring.henallux.firstspringproject.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		// Allow the default CSRF configuration
		http.csrf(Customizer.withDefaults());

		// Configure the filter chain to allow requests to the provided endpoints
		http
			.authorizeHttpRequests(
				(httpRequest) -> {
					httpRequest
						.anyRequest()
						.permitAll();
				}
			);

		return http.build();
	}
}
