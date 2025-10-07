package com.omni.main.ValidateUserNames;

import com.omni.main.ValidateUserNames.dto.ApiResponse;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DTO_Test {

    @Test
    void testSuccessResponse() {
        String data = "User created";
        ApiResponse<String> response = ApiResponse.success(data);

        assertNotNull(response);
        assertEquals("000000", response.getStatus().getCode());
        assertEquals("SUCCESS", response.getStatus().getDescription());
        assertEquals("User created", response.getData());
    }

    @Test
    void testNoDataResponse() {
        ApiResponse<Object> response = ApiResponse.noData();

        assertNotNull(response);
        assertEquals("000404", response.getStatus().getCode());
        assertEquals("No Data Found", response.getStatus().getDescription());
        assertEquals(Collections.emptyList(), response.getData());
    }

    @Test
    void testErrorResponse() {
        ApiResponse<Object> response = ApiResponse.error("Something went wrong");

        assertNotNull(response);
        assertEquals("000500", response.getStatus().getCode());
        assertEquals("Something went wrong", response.getStatus().getDescription());
        assertEquals(Collections.emptyList(), response.getData());
    }

    @Test
    void testCustomConstructorAndStatus() {
        ApiResponse.Status status = new ApiResponse.Status("123456", "CUSTOM");
        ApiResponse<String> response = new ApiResponse<>(status, "Data");

        assertEquals("123456", response.getStatus().getCode());
        assertEquals("CUSTOM", response.getStatus().getDescription());
        assertEquals("Data", response.getData());
    }
}
