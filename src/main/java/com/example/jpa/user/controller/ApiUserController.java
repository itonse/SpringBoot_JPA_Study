package com.example.jpa.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.example.jpa.notice.entity.Notice;
import com.example.jpa.notice.entity.NoticeLike;
import com.example.jpa.notice.model.NoticeResponse;
import com.example.jpa.notice.model.ResponseError;
import com.example.jpa.notice.repository.NoticeLikeRepository;
import com.example.jpa.notice.repository.NoticeRepository;
import com.example.jpa.user.entity.User;
import com.example.jpa.user.exception.ExistsEmailException;
import com.example.jpa.user.exception.PasswordNotMatchException;
import com.example.jpa.user.exception.UserNotFoundException;
import com.example.jpa.user.model.*;
import com.example.jpa.user.repository.UserRepository;
import com.example.jpa.util.JWTUtils;
import com.example.jpa.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor  // (중요) 자동으로 필요한 생성자를 만들어서 userRepository 에 주입.
@RestController
public class ApiUserController {

    // 레파지토리 사용. 클래스에 @RequiredArgsConstructor 어노테이션 붙이기
    private final UserRepository userRepository;
    private final NoticeRepository noticeRepository;
    private final NoticeLikeRepository noticeLikeRepository;

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

    /* // 32번 (이메일 중복 오류 처리X 36번에서 처리)
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
    */

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

    @ExceptionHandler(value = {ExistsEmailException.class, PasswordNotMatchException.class})
    public ResponseEntity<?> TwoExceptionHandler(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    /* // 36번
    @PostMapping("/api/user")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserInput userInput, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError)e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        if (userRepository.countByEmail(userInput.getEmail()) > 0) {  // 입력한 이메일과 동일한 이메일이 이미 등록되어 있으면,
            throw new ExistsEmailException("이미 존재하는 이메일 입니다.");
        }

        User user = User.builder()
                .email(userInput.getEmail())
                .userName(userInput.getUserName())
                .phone(userInput.getPhone())
                .password(userInput.getPassword())
                .regDate(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
    */

    // 37번 비밀번호 변경
    // PasswordNotMatchException 익셉션 핸들러는 위의 핸들러에 같이 정의함.

    @PatchMapping("/api/user/{id}/password")
    public ResponseEntity<?> updateUserPassword(@PathVariable Long id, @RequestBody @Valid UserInputPassword userInputPassword, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError)e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByIdAndPassword(id, userInputPassword.getPassword())
                .orElseThrow(() -> new PasswordNotMatchException("비밀번호가 일치하지 않습니다."));

        user.setPassword(userInputPassword.getNewPassword());
        user.setUpdateDate(LocalDateTime.now());

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    // 38번 패스워드 암호화
    private String getEncryptPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(); // 스프링에서 제공하는 암호 인코더(BCrypt 해시 알고리즘 사용)
        return bCryptPasswordEncoder.encode(password);
    }

    @PostMapping("/api/user")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserInput userInput, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError)e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        if (userRepository.countByEmail(userInput.getEmail()) > 0) {  // 입력한 이메일과 동일한 이메일이 이미 등록되어 있으면,
            throw new ExistsEmailException("이미 존재하는 이메일 입니다.");
        }

        String encryptPassword = getEncryptPassword(userInput.getPassword());

        User user = User.builder()
                .email(userInput.getEmail())
                .userName(userInput.getUserName())
                .phone(userInput.getPhone())
                .password(encryptPassword)
                .regDate(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    // 39번
    @DeleteMapping("/api/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        // 내가 쓴 공지사항이 있는 경우
        // 1. 삭제 못해.. 삭제하려면, 공지사항 삭제하고와..
        // 2. 회원삭제전에 공지사항글을 다 삭제하는 경우..
        // 3. 그 외 여러가지

        try {
            userRepository.delete(user);
        } catch (DataIntegrityViolationException e) {  // 해당 id 를 참조하고 있는 게시글이 존재하면 에러발생
            String message = "제약조건에 문제가 발생하였습니다.";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } catch (Exception e) { // 그 외 다른 에러들이 있으면 잡기
            String message = "회원 탈퇴 중 문제가 발생하였습니다.";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().build();
    }

    // 40 사용자 아이디(이메일) 찾기
    @GetMapping("/api/user/find")
    public ResponseEntity<?> findUser(
            @RequestBody @Valid userInputFind userInputFind, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError)e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByUserNameAndPhone(userInputFind.getUserName(), userInputFind.getPhone())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        UserResponse userResponse = UserResponse.of(user);

        return ResponseEntity.ok().body(userResponse);  // 응답시 보안상의 이유로 user 정보를 다 내리지 않고, userResponse 로 몇몇 정보들만 내리기
    }

    // 41 비밀번호 초기화
    private String getResetPassword() {    // UUID -> 고유 식별자
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);  // UUID 생성하여 '-' 는 제거하고, 상위 10자리만 리턴
    }

    void sendSMS(String message) {
        System.out.println("[문자메시지전송]");
        System.out.println(message);
    }

    @GetMapping("/api/user/{id}/password/reset")
    public ResponseEntity<?> resetUserPassword(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        // 비밀번호 초기화
        String resetPassword = getResetPassword();
        String resetEncryptPassword = getEncryptPassword(resetPassword);   // 초기화 된 비밀번호를 암호화 처리
        user.setPassword(resetEncryptPassword);
        userRepository.save(user);

        // 문자메세지 보내기 (여기서는 콘솔에 찍는 것으로 대체)
        String message = String.format(   // 포맷 형식
                "[%s]님의 임시비밀번호가 [%s]로 초기화 되었습니다.", user.getUserName(), resetPassword);
        sendSMS(message);

        return ResponseEntity.ok().build();
    }

    // 42번 해당 유저가 좋아요한 공지사항 보기
    @GetMapping("api/user/{id}/notice/like")
    public List<NoticeLike> likeNotice(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다,"));

        List<NoticeLike> noticeLikeList = noticeLikeRepository.findByUser(user);
        return noticeLikeList;    // 출력 내용: {n번째 글의 작성자, n번째 글, 좋아요한 사람} 순서로
    }

    /* // 43번 토큰발행 준비
    @PostMapping("/api/user/login")
    public ResponseEntity<?> createToken(@RequestBody @Valid UserLogin userLogin, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError)e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(() -> new UserNotFoundException("이메일에 해당하는 유저가 없습니다."));

        // 비밀번호 검증 (암호화가 안된 userLogin 패스워드랑 암호화 된 user 패스워드 매칭)
        if (!PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        return ResponseEntity.ok().build();
    }
    */

    /* // 44번 토큰발행 시점 (43번 이어서)
    @PostMapping("/api/user/login")
    public ResponseEntity<?> createToken(@RequestBody @Valid UserLogin userLogin, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError)e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(() -> new UserNotFoundException("이메일에 해당하는 유저가 없습니다."));

        if (!PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        // 토큰발행
        String token = JWT.create()   // 토큰 객체생성
                .withExpiresAt(new Date())   // 토큰 유효기간(현재)
                .withClaim("user_id", user.getId())  // 토큰에 추가정보 넣기
                .withSubject(user.getUserName())   // 토큰이 어떤 주체에 대해 유효한지
                .withIssuer(user.getEmail())   // 토큰 발급 주체
                .sign(Algorithm.HMAC512("fastcampus".getBytes()));  // HMAC512 알고리즘과 비밀키(fastcampus)를 사용하여 서명. (비밀키로 JWT 유효성 검증가능)

        return ResponseEntity.ok().body(
                UserLoginToken.builder().token(token).build());
    }
    */

    // 45번 토큰 유효기간 설정
    @PostMapping("/api/user/login")
    public ResponseEntity<?> createToken(@RequestBody @Valid UserLogin userLogin, Errors errors) {

        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().stream().forEach((e) -> {
                responseErrorList.add(ResponseError.of((FieldError)e));
            });
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        if (!PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        // 토큰 유효기간으로 사용할 Date 객체 만들기
        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);   // LocalDateTime을 이용해 Date 만들기 (현재 날짜에서 1달 뒤)
        Date expiredDate = java.sql.Timestamp.valueOf(expiredDateTime); // LocalDateTime -> Date 형태 변환

        // 토큰발행
        String token = JWT.create()   // 토큰 객체생성
                .withExpiresAt(expiredDate)   // 토큰 유효기간
                .withClaim("user_id", user.getId())  // 토큰에 추가정보 넣기
                .withSubject(user.getUserName())   // 토큰이 어떤 주체에 대해 유효한지
                .withIssuer(user.getEmail())   // 토큰 발급 주체
                .sign(Algorithm.HMAC512("fastcampus".getBytes()));  // HMAC512 알고리즘과 비밀키(fastcampus)를 사용하여 서명. (비밀키로 JWT 유효성 검증가능)

        return ResponseEntity.ok().body(
                UserLoginToken.builder().token(token).build());
    }

    // 46번 JWT 토큰 재발행
    @PatchMapping("/api/user/login")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {  // 토큰은 일반적으로 Header 에 보내므로, 요청헤더에서 Header 의 데이터를 꺼내옴

        String token = request.getHeader("F-TOKEN");  // 헤더에서 토큰의 값을 받음
        String email = "";

        // 토큰의 유효성을 검증하고, 해당 토큰의 User 정보를 가져와야 하기 때문에 토큰 발급주체인 email 이 필요
        try {
            email = JWT.require(Algorithm.HMAC512("fastcampus".getBytes()))
                    .build()
                    .verify(token) // 토큰 검증
                    .getIssuer();   // Issuer 에 이메일이 있음 (여기서 email과 Issuer는 완전히 동일)
        } catch(SignatureVerificationException e) {   // 정상적인 토큰이 아니라면 예외 잡아서 던지기
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));

        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);   // LocalDateTime을 이용해 Date 만들기 (현재 날짜에서 1달 뒤)
        Date expiredDate = java.sql.Timestamp.valueOf(expiredDateTime);

        // 토큰 재발행
        String newToken = JWT.create()   // 토큰 객체생성
                .withExpiresAt(expiredDate)   // 토큰 유효기간
                .withClaim("user_id", user.getId())  // 토큰에 추가정보 넣기
                .withSubject(user.getUserName())   // 토큰이 어떤 주체에 대해 유효한지
                .withIssuer(user.getEmail())   // 토큰 발급 주체
                .sign(Algorithm.HMAC512("fastcampus".getBytes()));  // HMAC512 알고리즘과 비밀키(fastcampus)를 사용하여 서명. (비밀키로 JWT 유효성 검증가능)

        return ResponseEntity.ok().body(
                UserLoginToken.builder().token(newToken).build());
    }

    // 47번 JWT 토큰 삭제
    @DeleteMapping("/api/user/login")
    public ResponseEntity<?> removeToken(@RequestHeader("F-TOKEN") String token) {   // 요청 헤더에서 토큰을 받아옴

        String email = "";

        try {
            email = JWTUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {   // 정상적인 토큰이 아니라면 예외 잡아서 던지기
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // ** 토큰 삭제 부분 **
        // 세션, 쿠키삭제
        // 크랄이언트 쿠키/로컬스토리지/세셔늣토리지
        // 블랙리스트 작성


        return ResponseEntity.ok().build();
    }

}
