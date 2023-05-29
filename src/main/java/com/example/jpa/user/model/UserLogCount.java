package com.example.jpa.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserLogCount {
    private long id;  // 유저 id
    private String email;
    private String userName;

    private long noticeCount;  // 유저가 작성한 공지글 개수
    private long noticeLikeCount;
}
