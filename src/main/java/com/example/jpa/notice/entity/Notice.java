package com.example.jpa.notice.entity;

import com.example.jpa.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notice {   // Notice Model 과 다르게 DB 와 연동할 때 이용되는 엔티티

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne  // 한 사용자가 여러개의 글을 쓸 수 있음
    @JoinColumn  // User 엔티티과 Join 맺기 (스키마에서 외래키인 USER_ID는 User 의 PK인 id 참조)
    private User user;

    @Column
    private String title;

    @Column
    private String contents;

    @Column
    private LocalDateTime regDate;

    @Column
    private LocalDateTime updateDate;

    @Column
    private int hits;

    @Column
    private int likes;

    @Column
    private boolean deleted;

    @Column
    private LocalDateTime deletedDate;
}
