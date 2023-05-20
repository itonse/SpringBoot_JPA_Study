package com.example.jpa.notice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NoticeInput {  // 입력받는 값
    // ID 와 등록일은 입력받을 필요가 없으므로, 따로 클래스를 만듦.
    private String title;
    private String contents;
}
