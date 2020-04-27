package com.luv2code.springsecurity.demo.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class DemoSecurityConfig extends WebSecurityConfigurerAdapter {
	
	// add a reference to our security data source
	
	@Autowired
	private DataSource securityDataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		// use jdbc authentication ...
		
		auth.jdbcAuthentication().dataSource(securityDataSource);
		
		
		
		
		// add our user for in memory authentication
		
/*		UserBuilder users = User.withDefaultPasswordEncoder();
 
		auth.inMemoryAuthentication()
		.withUser(users.username("Rahul").password("123rahul").roles("EMPLOYEE"))
		.withUser(users.username("John").password("123john").roles("EMPLOYEE", "ADMIN"))
		.withUser(users.username("Arun").password("123arun").roles("EMPLOYEE", "MANAGER"));
*/		
		
		// TODO Auto-generated method stub
		/* super.configure(auth); */
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()				// restrict excess based on httpServletRequest
		.antMatchers("/").hasRole("EMPLOYEE")
		.antMatchers("/leaders/**").hasRole("MANAGER")
		.antMatchers("/systems/**").hasRole("ADMIN")									//	.anyRequest().authenticated()  (I am deleting this becouse i do not want to allow user based on only authentication now i want to verify authorization)	// Any request to the app must be authenticated(i.e logged in)
			.and()								
			.formLogin()						// And for form login, we are customizing form login process. We need to create Controller for this request mapping   
				.loginPage("/showMyLoginPage")	// for actual login page show our custome form at the request mapping (i.e /showMyLoginPage)  
				.loginProcessingUrl("/authenticateTheUser") // Login form should POST data to this url for processing (check userId and password). No controller request mapping required for this.      
				.permitAll()					// Allow every one to see login page. No need to login   
			.and()
			.logout().permitAll()				// Logout support for users
			.and()
			.exceptionHandling().accessDeniedPage("/access-denied");
	}

}
