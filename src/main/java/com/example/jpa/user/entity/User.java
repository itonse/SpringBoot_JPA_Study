package com.example.jpa.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
public class User {

    @Id   // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // PK 키는 자동 증가하여 생성
    private Long id;

    @Column
    private String email;

    @Column
    private String userName;

    @Column
    private String password;

    @Column
    private String phone;

    @Column
    private LocalDateTime regDate;

    @Column
    private LocalDateTime updateDate;
}
