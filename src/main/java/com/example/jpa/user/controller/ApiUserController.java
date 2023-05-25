package com.example.jpa.user.controller;

import com.example.jpa.notice.entity.Notice;
import com.example.jpa.notice.model.NoticeResponse;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.notice.repository.NoticeRepository;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.exception.UserNotFoundException;
import com.example.jpa.user.model.UserInput;
import com.example.jpa.user.model.UserResponse;
import com.example.jpa.user.model.UserUpdate;
import com.example.jpa.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor  // (중요) 자동으로 필요한 생성자를 만들어서 userRepository 에 주입.
@RestController
public class ApiUserController {

    private final UserRepository userRepository;  // 레파지토리 사용. 클래스에 @RequiredArgsConstructor 어노테이션 붙이기
    private final NoticeRepository noticeRepository;

    /* // 31번
    @PostMapping("/api/user")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserInput userInput, Errors errors) {
        // ResponseEntity<?> 는 HTTP 응답을 나타내는 객체로, 상태코드, 헤더 및 본문을 포함할 수 있음. ?는 제네릭 타입으로, 반환할 데이터의 타입 지정X
        // @Valid 가 userInput 객체의 유효성 검증을 하고, errors 는 발생하는 유효성 검증 에러를 수집함.

        List<ResponseError> responseErrorList = new ArrayList<>();   // 에러 클래스는 notice 에서 생성했던 것 이용

        if (errors.hasErrors()) {  // 에러가 있을 때
            errors.getAllErrors().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError) e));  // 에러 목록들이 쌓임
            });

            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);  // 에러 목록들 던짐 (400 에러)
        }

        return ResponseEntity.ok().build();   // 에러가 없는 경우, HTTP 상태코드 200(OK)을 반환하는 응답 생성
    }
    */

    // 32번 (이메일 중복 오류 처리X)
    @PostMapping("/api/user")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserInput userInput, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<>();

        if (errors.hasErrors()) {  // 에러가 있을 때
            errors.getAllErrors().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError) e));  // 에러 목록들이 쌓임
            });

            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);  // 에러 목록들 던짐 (400 에러)
        }

        User user = User.builder()
                .email(userInput.getEmail())
                .userName(userInput.getUserName())
                .password(userInput.getPassword())
                .phone(userInput.getPhone())
                .regDate(LocalDateTime.now())
                .build();

        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    // 33번
    @ExceptionHandler(UserNotFoundException.class)   // UserNotFoundException 예외처리 담당
    public ResponseEntity<?> UserNotFoundExceptionHandler(UserNotFoundException exception) {    // 예외 캐치
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/api/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id
                           , @RequestBody @Valid UserUpdate userUpdate, Errors errors) {  // userUpdate 는 @RequestBody 로 받아야 함.

        List<ResponseError> responseErrorList = new ArrayList<>();

        if (errors.hasErrors()) {  // 에러가 있을 때
            errors.getAllErrors().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError) e));  // 에러 목록들이 쌓임
            });

            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);  // 에러 목록들 던짐 (400 에러)
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        user.setPhone(userUpdate.getPhone());
        user.setUpdateDate(LocalDateTime.now());

        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    // 34번
    @GetMapping("/api/user/{id}")
    public UserResponse getUser(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        //UserResponse userResponse = new UserResponse(user);  // 방법1
        UserResponse userResponse = UserResponse.of(user);  // 방법2 (추천)

        return userResponse;
    }

    // 35번
    @GetMapping("/api/user/{id}/notice")
    public List<NoticeResponse> userNotice(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        List<Notice> noticeList = noticeRepository.findByUser(user);

        List<NoticeResponse> noticeResponseList = new ArrayList<>();

        noticeList.stream().forEach((e) -> {
            noticeResponseList.add(NoticeResponse.of(e));
        });

        return noticeResponseList;
    }
}
