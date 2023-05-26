package com.example.jpa.notice.entity;

import com.example.jpa.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NoticeLike {   // Notice Model 과 다르게 DB 와 연동할 때 이용되는 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn
    @ManyToOne   // 공지 하나당 여러개의 좋아요가 쌓일 수 있음
    private Notice notice;

    @JoinColumn
    @ManyToOne  // 한 사용자가 여러개의 글을 쓸 수 있음
    private User user;

}
