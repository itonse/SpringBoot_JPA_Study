package com.example.jpa.notice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ResponseError {
    private String field;
    private String message;

    public static ResponseError of(FieldError e) {   // 에러 하나에 대해서
        return ResponseError.builder()
                .field(e.getField())
                .message(e.getDefaultMessage())
                .build();
    }

    public static List<ResponseError> of(List<ObjectError> errors) {  // 에러 여러개에 대해서

        List<ResponseError> responseErrors = new ArrayList<>();

        if (errors != null) {
            errors.stream().forEach((e) -> {   // 모든 에러들을 돌면서 취합함
                responseErrors.add(ResponseError.of((FieldError)e));  // 에러 추가
            });
        }

        return responseErrors;
    }
}