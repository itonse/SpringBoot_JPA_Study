package com.example.jpa.user.repository;

import com.example.jpa.user.entity.User;
import com.example.jpa.user.model.UserNoticeCount;
import com.example.jpa.user.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {   // CRUD 기능이 있는 JPA 레파지토리를 상속 받음

    // 인터페이스의 메소드 형식: 반환타입 함수명(매개변수)
    // JPA 레파지토리에 의해 메소드의 이름을 분석하여 자동으로 SQL 쿼리를 생성하고 실행

    int countByEmail(String email);

    Optional<User> findByIdAndPassword(Long id, String password);

    Optional<User> findByUserNameAndPhone(String userName, String phone);

    Optional<User> findByEmail(String email);

    List<User> findByEmailContainsOrPhoneContainsOrUserNameContains(
            String email, String phone, String userName);

    long countByStatus(UserStatus userStatus);   // UserStatus 는 enum

    // JPQL: JPA 에서 자동이 아닌 '직접적인' 쿼리 사용
    @Query (" select u from User u where u.regDate between :startDate and :endDate ")
    List<User> findToday(LocalDateTime startDate, LocalDateTime endDate);


}
