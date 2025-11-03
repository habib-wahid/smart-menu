package org.example.menuapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private boolean success;
    private String message;
    private String tracerId;
    private Object data;

    public ApiResponse(boolean success, String message, String tracerId) {
        this.success = success;
        this.message = message;
        this.tracerId = tracerId;
        this.data = null;
    }

    public static ApiResponse success(String message, String tracerId) {
        return new ApiResponse(true, message, tracerId);
    }

    public static ApiResponse success(String message, String tracerId, Object data) {
        return new ApiResponse(true, message, tracerId, data);
    }

    public static ApiResponse error(String message, String tracerId) {
        return new ApiResponse(false, message, tracerId);
    }

    public static ApiResponse error(String message, String tracerId, Object data) {
        return new ApiResponse(false, message, tracerId, data);
    }
}