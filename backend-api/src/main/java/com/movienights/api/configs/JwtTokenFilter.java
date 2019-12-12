package com.movienights.api.configs;

import com.movienights.api.exceptions.CustomException;
import com.movienights.api.services.LogServices;
import com.movienights.api.tokenproviders.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;
    private LogServices logServices;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, LogServices logServices) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.logServices = logServices;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        if(
                !httpServletRequest.getRequestURI().equalsIgnoreCase("/api/auth/refresh")
        ){
            try {
                if (token != null && !token.isEmpty() && jwtTokenProvider.validateToken(token)) {
                    Authentication auth = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (CustomException ex) {
                SecurityContextHolder.clearContext();
                httpServletResponse.sendError(ex.getHttpStatus().value(), ex.getMessage());
                logServices.Loga(httpServletRequest,httpServletResponse);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        logServices.Loga(httpServletRequest,httpServletResponse);

    }

}
