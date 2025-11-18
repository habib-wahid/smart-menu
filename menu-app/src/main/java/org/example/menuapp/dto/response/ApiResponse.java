package org.example.menuapp.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private String timeStamp;
    private T data;
    private List<ErrorDetail> error;

    public ApiResponse() {
        this.timeStamp = LocalDateTime.now().toString();
        this.error = new ArrayList<>();
    }

    public ApiResponse(boolean success, String message, T data) {
        this();
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Successfully completed the operation", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    public static <T> ApiResponse<T> error(List<ErrorDetail> error) {
        ApiResponse<T> response = new ApiResponse<>(false, "Operation Failed", null);
        response.setError(error);
        return response;
    }
}
