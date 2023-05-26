package com.example.jpa.notice.repository;

import com.example.jpa.notice.entity.NoticeLike;
import com.example.jpa.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeLikeRepository extends JpaRepository<NoticeLike, Long> {

    List<NoticeLike> findByUser(User user);
}
