package com.whimmy.revenue.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("1").password("1").roles("USER");
        auth.inMemoryAuthentication().withUser("2").password("2").roles("ADMIN");
    }

    // 该方法修改不能热部署
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
	    http
	    	.csrf()
	    		.disable()
	    	.formLogin()
	    		.loginPage("/login")
	    		.failureUrl("/login?error").usernameParameter("username").passwordParameter("password")
	    		.and()
	    	.logout()
            	.logoutUrl("/logout")
            	.logoutSuccessUrl("/login")
            	.and()
	        .authorizeRequests()
	        	.antMatchers("/resource/**", "/login", "/login.html", "/", "/home.html").permitAll()
	        	.antMatchers("/admin.html").hasRole("ADMIN")
	            .anyRequest().authenticated()
            .and()
            	.exceptionHandling().accessDeniedPage("/403");
	    // @formatter:on
    }
}