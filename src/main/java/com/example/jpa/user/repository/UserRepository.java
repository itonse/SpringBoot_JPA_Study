package com.example.jpa.user.repository;

import com.example.jpa.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {   // CRUD 기능이 있는 JPA 레파지토리를 상속 받음

    // 인터페이스의 메소드 형식: 반환타입 함수명(매개변수)
    // JPA 레파지토리에 의해 메소드의 이름을 분석하여 자동으로 SQL 쿼리를 생성하고 실행

    int countByEmail(String email);

    Optional<User> findByIdAndPassword(Long id, String password);

    Optional<User> findByUserNameAndPhone(String userName, String phone);

    Optional<User> findByEmail(String email);
}
