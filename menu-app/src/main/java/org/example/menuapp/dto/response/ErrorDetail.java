package org.example.menuapp.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDetail {
    private String field;
    private String code;
    private String message;

    public ErrorDetail() {
    }

    public ErrorDetail(String field, String code, String message) {
        this.field = field;
        this.code = code;
        this.message = message;
    }
}
