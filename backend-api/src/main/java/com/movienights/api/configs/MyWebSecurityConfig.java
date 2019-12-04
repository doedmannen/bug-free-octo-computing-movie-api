package com.movienights.api.configs;

import com.movienights.api.tokenproviders.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MyUserDetailService userDetailService;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Bean("authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception { return super.authenticationManagerBean(); }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // No fuckery allowed
        http.csrf().disable();

        // Stateless
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Our entrypoints
        http.authorizeRequests()
                // Start of very specific paths in API
                    // Signin
                    .antMatchers("/api/auth/login").permitAll()
                .antMatchers("/api/auth/open").permitAll()
                // End of very specific paths in API
                // Start of general api security
                .antMatchers("/api/**").hasRole("USER")
                // End of general api security
                // Start of general all other paths
                .antMatchers("/**").permitAll()
                // End of general all other paths
                ;

        http.exceptionHandling().accessDeniedPage("/login");

        http.apply(new JwtTokenFilterConfig(tokenProvider));
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailService)
                .passwordEncoder(userDetailService.getEncoder());
    }
}
