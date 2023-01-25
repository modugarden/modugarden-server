package com.modugarden.config;

import com.modugarden.domain.auth.CustomUserDetailService;
import com.modugarden.utils.jwt.JwtAccessDeniedHandler;
import com.modugarden.utils.jwt.JwtAuthenticationEntryPoint;
import com.modugarden.utils.jwt.JwtAuthenticationFilter;
import com.modugarden.utils.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // Spring Security 설정을 시작
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final CustomUserDetailService customUserDetailService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin().disable()
                .httpBasic().disable()
                .cors().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 원하는 부분 주석 해제하면 로그인 하지 않고 이용가능하도록 설정 가능
                //.antMatchers("/users/**").permitAll()
                //.antMatchers("/follow/**").permitAll()
                //.antMatchers("/boards/**").permitAll()
                .antMatchers("/curations/**").permitAll()
                .antMatchers("/h2/**").permitAll()
                .antMatchers("/users/log-in", "/users/sign-up/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)// jwt 커스텀 필터 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);

        return http.build();
    }

    // 패스워드 인코더
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 이걸 설정하지 않아도 된다는 말도 있긴함
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService).passwordEncoder(new BCryptPasswordEncoder());
    }
    // 정적 자원들에 대한 요청 무시는 아직 설정하지 않았음.
}
