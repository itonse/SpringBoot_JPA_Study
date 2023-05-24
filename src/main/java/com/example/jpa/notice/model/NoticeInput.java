package com.example.jpa.notice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NoticeInput {  // 입력받는 값
    // ID 와 등록일은 입력받을 필요가 없으므로, 따로 클래스를 만듦.
    @Size(min = 10, max = 100, message = "제목은 10-100자 사이의 값입니다.")
    @NotBlank(message = "제목은 필수 항목입니다.")
    private String title;

    @Size(min = 50, max = 1000, message = "내용응 50-1000자 사이의 값입니다.")
    @NotBlank(message = "내용은 필수 항목입니다.")
    private String contents;
}
