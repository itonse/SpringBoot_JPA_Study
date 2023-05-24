package com.example.jpa.notice.repository;

import com.example.jpa.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository   // 레파지토리
public interface NoticeRepository extends JpaRepository<Notice, Long> {   // JPA 레파지토리 상속 받음, <엔티티, 엔티티의 PK 타입>

    Optional<List<Notice>> findByIdIn(List<Long> idList);  // 값이 없을수도 있으니 Optional 로 감싸기
}
