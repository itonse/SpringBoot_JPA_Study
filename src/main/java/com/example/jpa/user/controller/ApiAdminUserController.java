package com.example.jpa.user.controller;

import com.example.jpa.notice.repository.NoticeRepository;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.entity.UserLoginHistory;
import com.example.jpa.user.model.*;
import com.example.jpa.user.repository.UserLonginHistoryRepository;
import com.example.jpa.user.repository.UserRepository;
import com.example.jpa.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ApiAdminUserController {   // User 관리자 기능 컨트롤러

    // 레파지토리
    private final UserRepository userRepository;   // 유저레파지토리 인터페이스 사용
    private final NoticeRepository noticeRepository;  // 노티스레파지토리 인터페이스 사용
    private final UserLonginHistoryRepository userLonginHistoryRepository;

    // 서비스
    private final UserService userService;

    /*
    // 48번 {사용자 목록, 사용자 수} 내리기
    @GetMapping("/api/admin/user")
    public ResponseMessage userList() {

        long totalUserCount = userRepository.count();
        List<User> userList = userRepository.findAll();

        return ResponseMessage.builder()
                .totalCount(totalUserCount)
                .body(userList)
                .build();
    }
    */

    // 49번 사용자 상세 조회
    @GetMapping("/api/admin/user/{id}")
    public ResponseEntity<?> userDetail(@PathVariable Long id) {

        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            // 이제 UserNotFoundException 예외 던지지 않고, 만들어놓은 ResponseMessage 활용하기.
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }


        return ResponseEntity.ok().body(ResponseMessage.success(user));
    }

    // 50번 유저 검색
    @GetMapping("/api/admin/user/search")
    public ResponseEntity<?> findUser(@RequestBody UserSearch usersearch) {

        List<User> userList = userRepository.findByEmailContainsOrPhoneContainsOrUserNameContains(
                usersearch.getEmail(), usersearch.getPhone(), usersearch.getUserName());

        return ResponseEntity.ok().body(ResponseMessage.success(userList));
    }

    // 51번 유저 상태정보 변경
    @PatchMapping("api/admin/user/{id}/status")
    public ResponseEntity<?> userStatus(@PathVariable Long id, @RequestBody UserStatusInput userStatusInput) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        User user = optionalUser.get();

        user.setStatus(userStatusInput.getStatus());
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    // 52번 사용자 정보 삭제
    @DeleteMapping("/api/admin/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        User user = optionalUser.get();

        long noticeCount = noticeRepository.countByUser(user);

        if (noticeCount > 0) {
            return new ResponseEntity<>(ResponseMessage.fail("사용자가 작성한 공지사항이 있습니다."), HttpStatus.BAD_REQUEST);
        }

        userRepository.delete(user);

        return ResponseEntity.ok().build();
    }

    // 53번 유저 접속이력 조회
    @GetMapping("/api/admin/user/login/history")   // 모든 사용자의 접속이력 조회
    public ResponseEntity<?> userLoginHistory() {

        List<UserLoginHistory> userLoginHistories = userLonginHistoryRepository.findAll();

        return ResponseEntity.ok().body(userLoginHistories);
    }

    // 54번 유저 접속제한
    @PatchMapping("/api/admin/user/{id}/lock")
    public ResponseEntity<?> userLock(@PathVariable Long id) {

        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        User user = optionalUser.get();

        if (user.isLockYn()) {
            return new ResponseEntity<>(ResponseMessage.fail("이미 접속제한이 된 사용자 입니다."), HttpStatus.BAD_REQUEST);
        }

        user.setLockYn(true);
        userRepository.save(user);

        return ResponseEntity.ok().body(ResponseMessage.success(""));
    }

    // 55번 유저 접속제한 해제
    @PatchMapping("/api/admin/user/{id}/unlock")
    public ResponseEntity<?> userUnLock(@PathVariable Long id) {

        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(ResponseMessage.fail("사용자 정보가 존재하지 않습니다."), HttpStatus.BAD_REQUEST);
        }

        User user = optionalUser.get();

        if (!user.isLockYn()) {
            return new ResponseEntity<>(ResponseMessage.fail("이미 접속제한이 해제된 사용자 입니다."), HttpStatus.BAD_REQUEST);
        }

        user.setLockYn(false);
        userRepository.save(user);

        return ResponseEntity.ok().body(ResponseMessage.success(""));
    }

    // 56번 회원 전체수, 상태별 회원수 리턴
    @GetMapping("/api/admin/user/status/count")
    public ResponseEntity<?> userStatusCount() {
        UserSummary userSummary = userService.getUserStatusCount();

        return ResponseEntity.ok().body(ResponseMessage.success(userSummary));
    }

    // 57번 오늘 가입 사용자 목록 리턴
    @GetMapping("/api/admin/user/today")
    public ResponseEntity<?> todayUser() {

        List<User> users = userService.getTodayUsers();

        return ResponseEntity.ok().body(ResponseMessage.success(users));
    }

    // 58번 사용자별 게시글수 (커스텀 레파지토리에서 네이티브 쿼리 사용)
    @GetMapping("/api/admin/user/notice/count")
    public ResponseEntity<?> userNoticeCount() {
        List<UserNoticeCount> userNoticeCountList = userService.getUserNoticeCount();

        return ResponseEntity.ok().body(ResponseMessage.success(userNoticeCountList));
    }

    // 59번 사용자별 게시글수와 좋아요수
    @GetMapping("/api/admin/user/count")
    public ResponseEntity<?> userLogCount() {
        List<UserLogCount> userLogCounts = userService.getUserLogCount();

        return ResponseEntity.ok().body(ResponseMessage.success(userLogCounts));
    }

    // 60번 좋아요를 가장 많이 한 사용자 목록(10개) -> (쿼리작성연습)
    @GetMapping("/api/admin/user/like/best")
    public ResponseEntity<?> bestLikeCount() {
        List<UserLogCount> userLogCounts = userService.getUserLikeBest();

        return ResponseEntity.ok().body(ResponseMessage.success(userLogCounts));
    }
}
