package com.beyond.basic.b2_board.common.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//
@Configuration
public class SecurityConfig {

//    private final JwtTokenFilter jwtTokenFilter;
//
//    public SecurityConfig(JwtTokenFilter jwtTokenFilter) {
//        this.jwtTokenFilter = jwtTokenFilter;
//    }

    //    내가 만든 클래스와 객체는 @Component, 외부 클래스(라이브러리)를 활용한 객체는 @Configuration + @Bean
//    @Component는 클래스 위에 붙여 클래스기반에 객체를 싱글톤객체로 생성.
//    @Bean은 메서드위에 붙여 Return 되는 객체를 싱클톤객체로 생성.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
//                csrf공격(일반적으로 쿠키를 활용한 공격 공격)에 대한 방어 비활성화
                .csrf(AbstractHttpConfigurer::disable)
//                http basic은 email/pw를 인코디하여 인증(전송)하는 간단한 인증박식. 비활성화.
                .httpBasic(AbstractHttpConfigurer::disable)
//                세션로그인방식 비활성화
                .sessionManagement(a->a.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                token을 검증하고, Authentication객체 생성
//                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
//                지정한 특정 url을 제외한 모든 요청에 대해서 authenticated(인증처리)하겠다 라는 의미
                .authorizeHttpRequests(a->a.requestMatchers("/author/create","/author/login")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .build();
    }
    @Bean
    public PasswordEncoder pwEncoder(){

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
