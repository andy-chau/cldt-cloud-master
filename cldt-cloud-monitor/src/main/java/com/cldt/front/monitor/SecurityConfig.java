package com.cldt.front.monitor;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * The class Security config.
 *
 * @zhoukj
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	/**
	 * Configure.
	 *
	 * @param http the http
	 *
	 * @throws Exception the exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Page with login form is served as /login.html and does a POST on
		// /login
		http.formLogin().loginPage("/login.html").loginProcessingUrl("/login").permitAll();
		// The UI does a POST on /logout on logout
		http.logout().logoutUrl("/logout");
		// The ui currently doesn't support csrf
		http.csrf().disable();

		// Requests for the login page and the static assets are allowed
		http.authorizeRequests().antMatchers("/api/**", "/applications/**", "/api/applications/**", "/login.html", "/**/*.css", "/img/**", "/third-party/**").permitAll();
		// ... and any other request needs to be authorized
		http.authorizeRequests().antMatchers("/**").authenticated();

		// Enable so that the clients can authenticate via HTTP basic for
		// registering
		http.httpBasic();
	}
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.headers().frameOptions().disable()
//				.and()
//				.formLogin()
//				.loginPage("/login.html")
//				.loginProcessingUrl("/login")
//				.and()
//				.logout().logoutUrl("/logout")
//				.and()
//				.csrf().disable()
//				.authorizeRequests()
//				.antMatchers("/api/**", "/applications/**", "/api/applications/**", "/login.html", "/**/*.css", "/img/**", "/third-party/**")
//				.permitAll()
//				.anyRequest().authenticated();
//	}
}