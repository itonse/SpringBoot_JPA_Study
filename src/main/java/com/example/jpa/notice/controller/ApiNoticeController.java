package com.example.jpa.notice.controller;

import com.example.jpa.notice.entity.Notice;
import com.example.jpa.notice.exception.NoticeNotFoundException;
import com.example.jpa.notice.model.NoticeInput;
import com.example.jpa.notice.model.NoticeModel;  // 다른 패키지이므로, import 필요
import com.example.jpa.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    // 15번
    @PostMapping("/api/notice")
    public Notice addNotice(@RequestBody NoticeInput noticeInput) {   // @RequestBody 를 붙이지 않으면 JSON 을 class 에 매핑하는 부분이 누락되어, title 과 contents 값이 null 이 된다.
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

    // 16번
    @GetMapping("/api/notice/{id}")   // 경로변수 {id} 는 동적인 값
    public Notice notice(@PathVariable Long id) {   // @PathVariable: 설정한 id 값에 해당하는 경로변수 값을 가져옴
        Optional<Notice> notice = noticeRepository.findById(id);  // 찾은 결과가 null 이 될 수 있어서 Optional 타입의 값이 반환됨.
        if (notice.isPresent()) {  // 데이터가 null 이 아니면
            return notice.get();
        }

        return null;  // 데이터가 null 이면
    }

    /* 17번
    @PutMapping("/api/notice/{id}")
    public void updateNotice(@PathVariable Long id, @RequestBody NoticeInput noticeInput) {   // 수정할 글의 id와, 수정한 제목과내용 값을 받음

        Optional<Notice> notice = noticeRepository.findById(id);
        if (notice.isPresent()) {
            notice.get().setTitle(noticeInput.getTitle());
            notice.get().setContents(noticeInput.getContents());
            notice.get().setUpdateDate(LocalDateTime.now());

            noticeRepository.save(notice.get());  // notice 는 Optional 타입이라, .get() 을 하여 엔티티를 가져와서 저장
        }
    }
    */

    // 18번
    // 500 에러가 아닌 400 에러를 던지기 위해서 설정
    @ExceptionHandler(NoticeNotFoundException.class)
    public ResponseEntity<String> HandlerNoticeNotFoundException(NoticeNotFoundException exception) {  // 익셉션핸들러가 캐치함.
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);   // 에러 메세지 던지고, 400 Bad Request 를 던진다.
    }

    /*
    @PutMapping("/api/notice/{id}")
    public void updateNotice(@PathVariable Long id, @RequestBody NoticeInput noticeInput) {

//        // 방법1
//        Optional<Notice> notice = noticeRepository.findById(id);
//        if (!notice.isPresent()) {  // 예외 발생
//            throw new NoticeNotFoundException("공지사항의 글이 존재하지 않습니다.");  // 이 에러를 위의 익셉션핸들러가 잡음 (위로 가기)
//
//        }
//
//        notice.get().setTitle(noticeInput.getTitle());
//        notice.get().setContents(noticeInput.getContents());
//
//        noticeRepository.save(notice.get());


        // 방법2 (더 간단)
        Notice notice = noticeRepository.findById(id)  // null 값이 아니니 Optional 형식x
                        .orElseThrow(() -> new NoticeNotFoundException("공지사항의 글이 존재하지 않습니다."));

        notice.setTitle(noticeInput.getTitle());
        notice.setContents(noticeInput.getContents());

        noticeRepository.save(notice);
    }
    */

    // 19번
    @PutMapping("/api/notice/{id}")
    public void updateNotice(@PathVariable Long id, @RequestBody NoticeInput noticeInput) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoticeNotFoundException("공지사항의 글이 존재하지 않습니다."));

        notice.setTitle(noticeInput.getTitle());
        notice.setContents(noticeInput.getContents());
        notice.setUpdateDate(LocalDateTime.now());

        noticeRepository.save(notice);
    }

    // 20번
    @PatchMapping("api/notice/{id}/hits")  // Patch 메소드: 부분적인 수정이 필요할 때 사용
    public void noticeHits(@PathVariable Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new NoticeNotFoundException("공지사항의 글이 존재하지 않습니다."));

        notice.setHits(notice.getHits() + 1);

        noticeRepository.save(notice);
    }
}
