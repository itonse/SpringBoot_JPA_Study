package com.example.jpa.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.experimental.UtilityClass;

@UtilityClass    // 유틸리티 클래스이다.
public class JWTUtils {   // JWT 관련 유틸

    private static final String KEY = "fastcampus";   // 토큰의 키

    // 토큰에서 이메일이 들어있는 부분인 Issuer 가져오기
    public static String getIssuer(String token) {

        // 45번 에서 'email = Issuer' 로 설정했기 때문에, email 가져오는 코드 부분에서 email 을 issuer 로만 바꾸기
        String issuer = JWT.require(Algorithm.HMAC512(KEY.getBytes()))
                .build()
                .verify(token) // 토큰 검증
                .getIssuer();   // Issuer 에 이메일이 있음

        return issuer;
    }

}
