package com.practice.SpringSecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	/* Method 1 - Básic configuration
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
                .authorizeHttpRequests()
                .requestMatchers("/v1/index2").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin(login -> login.permitAll())
                .build();
	} */
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		
		return httpSecurity
				.authorizeHttpRequests( auth -> {
					auth.requestMatchers("/v1/index2").permitAll();
					auth.anyRequest().authenticated();
				})
				.formLogin()
					.successHandler(successHandler())	//redirect after login
				.permitAll()
				.and()
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)		//ALWAYS, IF_REQUIRED, NEVER, STATELESS
					.invalidSessionUrl("/login")
					.maximumSessions(1)
					.expiredUrl("/login")
					.sessionRegistry(sessionRegistry())
				.and()
				.sessionFixation()
					.migrateSession()		// migratesession(), newSession(), none()
				.and()
				.build();
	}

	/*Method to manage a succesful authentication*/
	public AuthenticationSuccessHandler successHandler() {
		
		return ((request, response, authentication) -> {
			response.sendRedirect("v1/session");
		});
	}
	
	/*To obtain the sesion data*/
	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}
}
