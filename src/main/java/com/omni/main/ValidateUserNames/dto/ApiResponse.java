package com.omni.main.ValidateUserNames.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
public class ApiResponse<T> {
    private Status status;
    private T data;

    public ApiResponse(Status status, T data) {
        this.status = status;
        this.data = data;
    }


    @Data
    @AllArgsConstructor
    public static class Status {  // <-- Make it static
        private String code;
        private String description;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(new Status("000000", "SUCCESS"), data);
    }

    public static <T> ApiResponse<T> noData() {
        return new ApiResponse<>(new Status("000404", "No Data Found"), (T) Collections.emptyList());
    }

    public static <T> ApiResponse<T> error(String description) {
        return new ApiResponse<>(new Status("000500", description), (T) Collections.emptyList());
    }


}