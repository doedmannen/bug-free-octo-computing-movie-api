package com.movienights.api.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {
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
}
