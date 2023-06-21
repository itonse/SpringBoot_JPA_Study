package com.example.jpa.user.model;

import com.example.jpa.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NotNull
@Builder
@Data
public class ResponseMessage {

    private ResponseMessageHeader header;
    Object body;

    public static ResponseMessage fail(String message, Object data) {  // fail 일 때 메세지
        return ResponseMessage.builder()
                .header(ResponseMessageHeader.builder()
                        .result(false)
                        .resultCode("")
                        .message(message)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build())
                .body(data)
                .build();
    }

    public static ResponseMessage fail(String message) {  // fail 일 때 메세지
// ~60번 까지 사용
//        return ResponseMessage.builder()
//                .header(ResponseMessageHeader.builder()
//                        .result(false)
//                        .resultCode("")
//                        .message(message)
//                        .status(HttpStatus.BAD_REQUEST.value())
//                        .build())
//                .body(null)
//                .build();

        return fail(message, null);
    }

    public static ResponseMessage success(Object data) {
        return ResponseMessage.builder()
                .header(ResponseMessageHeader.builder()
                        .result(true)
                        .resultCode("")
                        .message("")
                        .status(HttpStatus.OK.value())
                        .build())
                .body(data)
                .build();
    }

    public static ResponseMessage success() {
// ~60번 까지 사용
//        return ResponseMessage.builder()
//                .header(ResponseMessageHeader.builder()
//                        .result(true)
//                        .resultCode("")
//                        .message("")
//                        .status(HttpStatus.OK.value())
//                        .build())
//                .body(null)
//                .build();
        return success(null);
    }


//    48번 문제에는 이것들만 쓰임
//    private long totalCount;
//    private List<User> body;
}
