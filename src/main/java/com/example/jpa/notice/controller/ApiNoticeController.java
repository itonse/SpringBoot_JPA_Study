package com.example.jpa.notice.controller;

import com.example.jpa.notice.entity.Notice;
import com.example.jpa.notice.model.NoticeInput;
import com.example.jpa.notice.model.NoticeModel;  // 다른 패키지이므로, import 필요
import com.example.jpa.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor  // 자동으로 필요한 생성자를 만들어서 noticeRepository 에 주입.
@RestController
public class ApiNoticeController {

    private final NoticeRepository noticeRepository;  // DB에 저장할 것이라서, 레파지토리를 주입받음


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

    /* 11번
    //@RequestMapping(value = "/api/notice", method = RequestMethod.POST) 리퀘스트맵핑을 사용했을 때
    @PostMapping("/api/notice")  // 메소드가 다르기 때문에 위에 GET 에서 사용했던 동일한 주소 사용 가능
    public NoticeModel addNotice(@RequestParam String title, @RequestParam String contents) {
        NoticeModel notice = NoticeModel.builder()
                .id(1)
                .title(title)
                .contents(contents)
                .regDate(LocalDateTime.of(2023, 05, 19, 0, 0))
                .build();

        return notice;
    }
    */

    /* 12번
    @PostMapping("/api/notice")
    public NoticeModel addNotice(NoticeModel noticeModel) {
        noticeModel.setId(2);
        noticeModel.setRegDate(LocalDateTime.now());

        return noticeModel;
    }
    */

    /* 13번
    @PostMapping("/api/notice")
    public NoticeModel addNotice(@RequestBody NoticeModel noticeModel) {  // JSON 형태로 받기 때문에 매핑을 위해 @RequestBody 필요

        noticeModel.setId(3);
        noticeModel.setRegDate(LocalDateTime.now());

        return noticeModel;
    }
    */

    /* 14번
    @PostMapping("/api/notice")
    public Notice addNotice(@RequestBody NoticeInput noticeInput) {  // NoticeModel 모델을 받고, 엔티티 형태로 반환
        Notice notice = Notice.builder()   // 엔티티 생성
                // id는 자동증가이니, 셋팅 필요X
                .title(noticeInput.getTitle())
                .contents(noticeInput.getContents())
                .regDate(LocalDateTime.now())
                .build();

        noticeRepository.save(notice);  // 엔티티를 레파지토리를 통해 DB에 저장

        return notice;  // 저장된 엔티티 리턴
    }
    */

    @PostMapping("/api/notice")
    public Notice addNotice(@RequestBody NoticeInput noticeInput) {   // @RequestBody 를 붙이지 않으면 Json을 class에 매핑하는 부분이 누락되어, title 과 contents 값이 null 이 된다.
        Notice notice = Notice.builder()
                .title(noticeInput.getTitle())
                .contents(noticeInput.getContents())
                .regDate(LocalDateTime.now())
                .hits(0)
                .likes(0)
                .build();

        Notice resultNotice = noticeRepository.save(notice);   // 이렇게 받는 방법도 있음

        return resultNotice;
    }

}
