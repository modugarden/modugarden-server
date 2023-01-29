package com.modugarden.config;

import com.modugarden.domain.auth.CustomUserDetailService;
import com.modugarden.utils.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)// Controller에서 @PreAuthorize 사용해 권한 체크
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // Spring Security 설정을 시작
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final CustomUserDetailService customUserDetailService;

    private final ExceptionHandlerFilter exceptionHandlerFilter;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // swagger - permit url
    private static final String[] SWAGGER_PERMIT_URL_ARRAY = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/webjars/**",
            "/swagger/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    private static final String[] LOGIN_PERMIT_URL_ARRAY = {
            "/users/log-in/**",
            "/users/sign-up/**",
            "/users/nickname/isDuplicated"
    };

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
                .antMatchers(SWAGGER_PERMIT_URL_ARRAY).permitAll() // swagger setting
                // 원하는 부분 주석 해제하면 로그인 하지 않고 이용가능하도록 설정 가능
                //.antMatchers("/users/**").permitAll()
                //.antMatchers("/follow/**").permitAll()
                //.antMatchers("/boards/**").permitAll()
                .antMatchers("/curations/**").permitAll()
                .antMatchers("/h2/**").permitAll()
                .antMatchers(LOGIN_PERMIT_URL_ARRAY).permitAll() // 하위 계층의 구체적인 url 정보가 먼저 작성되어야함
                //.antMatchers("/users/**").hasAnyRole("GENERAL", "CURATOR")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)// jwt 커스텀 필터 추가
                .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class) // 서블릿 필터 에러 처리 핸들링
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
