package com.movienights.api.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MyUserDetailService userDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                // Start of very specific paths in API

                // End of very specific paths in API
                // Start of general api security
                .antMatchers("/api/**").hasRole("USER")
                // End of general api security
                // Start of general all other paths
                .antMatchers("/**").permitAll()
                // End of general all other paths

                .and().formLogin().permitAll().defaultSuccessUrl("/", true)
                .and().logout().permitAll().logoutSuccessUrl("/")
                .and().httpBasic()
                .and().csrf().disable();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailService)
                .passwordEncoder(userDetailService.getEncoder());
    }
}
