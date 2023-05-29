package com.example.jpa.user.model;

public enum UserStatus {

    None, Using, Stop;   // 0: none, 1: 정상, 2: 정지

    int value;

    UserStatus() {   // 생성자

    }

    public int getValue() {
        return this.value;
    }

}
