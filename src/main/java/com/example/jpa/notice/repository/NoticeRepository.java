package com.example.jpa.notice.repository;

import com.example.jpa.notice.entity.Notice;
import com.example.jpa.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository   // 레파지토리
public interface NoticeRepository extends JpaRepository<Notice, Long> {   // CRUD 기능이 있는 JPA 레파지토리를 상속 받음, <엔티티, 엔티티의 PK 타입>

    // 인터페이스의 메소드 형식: 반환타입 함수명(매개변수)
    // JPA 레파지토리에 의해 메소드의 이름을 분석하여 자동으로 SQL 쿼리를 생성하고 실행

    Optional<List<Notice>> findByIdIn(List<Long> idList);  // 값이 없을수도 있으니 Optional 로 감싸기

    // 제목동일, 내용동일, 등록시간이 체크시간보다 크다. -> 중복!
    Optional<List<Notice>> findByTitleAndContentsAndRegDateIsGreaterThanEqual(String title, String contents, LocalDateTime redDate);

    // JpaRepository 의 Spring Data JPA 가 메서드 이름을 분석하여 SELECT COUNT~ 쿼리를 자동으로 생성
    int countByTitleAndContentsAndRegDateIsGreaterThanEqual(String title, String contents, LocalDateTime regDate);

    List<Notice> findByUser(User user);    // (관계) user 가 작성한 notice 리스트를 가져오는 함수 (JPA 레파지토리의 Read 기능)

    long countByUser(User user);
}
