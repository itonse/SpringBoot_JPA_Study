package com.example.jpa.notice.controller;

import com.example.jpa.notice.model.NoticeModel;  // 다른 패키지이므로, import 필요
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ApiNoticeController {

    /* 6번
    @GetMapping("/api/notice")
    public String NoticeString() {

        return "공지사항입니다.";
    }
    */

    /* 7번
    @GetMapping("/api/notice1")
    public NoticeModel notice() {
        NoticeModel notice = new NoticeModel();
        LocalDateTime regDate = LocalDateTime.of(2021, 2, 8, 0, 0);

        notice.setId(1);
        notice.setTitle("공지사항입니다.");
        notice.setContents("공지사항 내용입니다.");
        notice.setRegDate(regDate);

        return notice;
    }
    */

    /* 8번
    @GetMapping("/api/notice")
    public List<NoticeModel> notice() {
        List<NoticeModel> noticeList = new ArrayList<>();

        NoticeModel notice1 = NoticeModel   // 빌더패턴으로 생성해서 리스트에 추가
                .builder()
                .id(1)
                .title("공지사항입니다.")
                .contents("공지사항내용입니다.")
                .regDate(LocalDateTime.of(2021, 1, 30,0, 0))
                .build();
        noticeList.add(notice1);

        noticeList.add(NoticeModel.builder()   // 추가와 생성 한번에 하기
                .id(2)
                .title("두번째 공지사항입니다.")
                .contents("두번째 공지사항내용입니다.")
                .regDate(LocalDateTime.of(2021, 1, 31,0, 0))
                .build());

        return  noticeList;
    }
    */

    // 9번
    @GetMapping("/api/notice")
    public List<NoticeModel> notice() {
        List<NoticeModel> noticeList = new ArrayList<>();

        return noticeList;
    }

    // 10번
    @GetMapping("/api/notice/count")
    public int noticeCount() {

        return 10;  // 리턴 타입은 숫자지만, 웹에서는 문자로 출력된다.
    }
}
