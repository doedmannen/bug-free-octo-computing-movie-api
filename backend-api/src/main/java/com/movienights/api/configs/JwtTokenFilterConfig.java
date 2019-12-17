package com.movienights.api.configs;


import com.movienights.api.services.LogServices;
import com.movienights.api.tokenproviders.JwtTokenProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtTokenFilterConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private JwtTokenProvider jwtTokenProvider;
    private LogServices logServices;

    public JwtTokenFilterConfig(JwtTokenProvider jwtTokenProvider, LogServices logServices) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.logServices = logServices;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProvider,logServices);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}