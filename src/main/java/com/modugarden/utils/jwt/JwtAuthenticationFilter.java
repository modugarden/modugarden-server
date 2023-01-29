package com.modugarden.utils.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    public JwtAuthenticationFilter(TokenProvider provider) {
        tokenProvider = provider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = tokenProvider.resolveToken(request); // token 정보 획득

        if (token != null && tokenProvider.validateToken(token)) { // 토큰이 존재하고, 유효한 토큰이라면
            Authentication authentication = tokenProvider.getAuthentication(token); // 인증 정보 생성해서

            SecurityContextHolder.getContext().setAuthentication(authentication);// SecurityContext에 저장
        }

        filterChain.doFilter(request, response);

    }
}