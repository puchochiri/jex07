package org.zerock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.security.CustomLoginSuccessHandler;

import lombok.extern.log4j.Log4j;

@Configuration
@EnableWebSecurity 
//스프링 MVC와 스프링 시큐리티 결합 용도
@Log4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		log.info("configure..............................");
		auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("ADMIN");
		
		
//		auth.inMemoryAuthentication().withUser("member").password("{noop}member").roles("MEMBER");
		// 인코딩 member
		auth.inMemoryAuthentication().withUser("member").password("$2a$10$q7SkjJnfeOPpzqBphyIK4.MwjD6pGKLUKHekgcLvUfKQBtuzMRBme").roles("MEMBER");
		
	}
	
	@Bean
	public AuthenticationSuccessHandler loginSuccessHandler() {
		return new CustomLoginSuccessHandler();
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
			.antMatchers("/sample/all").permitAll()
			.antMatchers("/sample/admin").access("hasRole('ROLE_ADMIN')")
			.antMatchers("/sample/member").access("hasRole('ROLE_MEMBER')");
		
		// 로그인 페이지 이동
		http.formLogin().loginPage("/customLogin").loginProcessingUrl("/login").successHandler(loginSuccessHandler());
		http.logout().logoutUrl("/customLogout").invalidateHttpSession(true).deleteCookies("remember-me", "JSESSION_ID");
	}
	
	
	// PasswordEncoder
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}

}


