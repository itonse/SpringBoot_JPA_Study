package com.example.jpa.notice.entity;

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
