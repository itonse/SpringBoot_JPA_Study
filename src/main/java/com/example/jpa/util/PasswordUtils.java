package com.example.jpa.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCrypt;

@UtilityClass    // 유틸리티 클래스이다.
public class PasswordUtils {

    // 패스워드를 암호화해서 리턴하는 함수
    // 입력한 패스워드를 해시된 패스워드랑 비교하는 함수

    public boolean equalPassword(String password, String encryptedPassword) {

        return BCrypt.checkpw(password, encryptedPassword);   // 암호화가 안된 userLogin 패스워드랑 암호화 된 user 패스워드가 동일한건지 체크
    }
}
