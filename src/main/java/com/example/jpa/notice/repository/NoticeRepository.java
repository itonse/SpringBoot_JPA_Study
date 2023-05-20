package com.example.jpa.notice.repository;

import com.example.jpa.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository   // 레파지도리
public interface NoticeRepository extends JpaRepository<Notice, Long> {   // JPA 레파지토리 상속 받음, <엔티티, 엔티티의 PK 타입>
}
