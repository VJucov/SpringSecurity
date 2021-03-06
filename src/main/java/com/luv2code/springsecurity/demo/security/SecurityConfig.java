package com.luv2code.springsecurity.demo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        User.UserBuilder users = User.withDefaultPasswordEncoder();

        auth.inMemoryAuthentication()
                .withUser(users.username("john").password("test").roles("EMPLOYEE"))
                .withUser(users.username("mary").password("test").roles("EMPLOYEE","MANAGER"))
                .withUser(users.username("susan").password("test").roles("EMPLOYEE","MANAGER","ADMIN"))
                .withUser((users.username("lily").password("test").roles("EMPLOYEE","MANAGER","ADMIN","DIRECTOR")));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").hasAnyRole("EMPLOYEE")
                .antMatchers("/leaders/**").hasAnyRole("MANAGER")
                .antMatchers("/systems/**").hasAnyRole("ADMIN")
                .antMatchers("/directors/**").hasAnyRole("DIRECTOR")
                .and()
                .formLogin()
                .loginPage("/showMyLoginPage")
                .loginProcessingUrl("/authenticateTheUser")
                .permitAll()
                .and().logout().permitAll()
                .and().exceptionHandling().accessDeniedPage("/access-denied");
    }
}
