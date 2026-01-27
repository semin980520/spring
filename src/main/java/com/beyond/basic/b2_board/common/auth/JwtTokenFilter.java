package com.beyond.basic.b2_board.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//토큰 검증 후 Authentication객체 생성
@Component
public class JwtTokenFilter extends GenericFilter {

    @Value("${jwt.secretKey}")
    private String st_secret_ket;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {


            HttpServletRequest req = (HttpServletRequest) servletRequest;
            String bearerToken = req.getHeader("Authorization"); //HttpServletRequest로 형 변환 후 Header에서 토큰 꺼내는 과정


            if (bearerToken == null) {
//            token이 없는 경우 검증을 할 수 없음으로, filter chain으로 return
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
//       Bearer문자열을 제거한 후에 jwt token만을 검증
            String token = bearerToken.substring(7);


//        token 검증 및 claims 추출
            Claims claims = Jwts.parserBuilder() // 페이로드 시크릿키 꺼네서 조합해서 시그니처와 맞는지 검증까지해줌
                    .setSigningKey(st_secret_ket)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

//        claims를 기반으로 Authentication객체 생성
//        권한의 경우 다수의 권한을 가질 수 있으므로 일반적으로 List로 설계
            List<GrantedAuthority> authorityList = new ArrayList<>();
//        권한을 세팅할 때 권한은 ROLE_라는 키워드를 붙임으로서 추후 권한체크 어노테이션사용 가능
            authorityList.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
//        1. pricipal : email 2. credentials : 토큰 3. authorities : 권한묶음
            Authentication authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorityList);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (Exception e){
            e.printStackTrace();
        }
        //        다시 filterChain으로 돌아가는 로직
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
