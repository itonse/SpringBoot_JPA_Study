package com.example.jpa.user.model;

import com.example.jpa.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserResponse {
    private long id;
    private String email;
    private String userName;
    protected String phone;

    /* 방법1
    public UserResponse(User user) {   // 클래스의 생성자라서 void 가 붙지 않음.
        this.id = user.getId();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.phone = user.getPhone();
    }
    */

    // 방법2: 요즘 많이 사용하는
    public static UserResponse of(User user) {  // 인스턴스 생성하지 않고 호출될 수 있도록 static 이 붙음.
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .phone(user.getPhone())
                .build();
    }
}
