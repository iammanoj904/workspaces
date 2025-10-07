package com.omni.main.ValidateUserNames;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omni.main.ValidateUserNames.controller.UsernameController;
import com.omni.main.ValidateUserNames.dto.ApiResponse;
import com.omni.main.ValidateUserNames.dto.UserCreateRequest;
import com.omni.main.ValidateUserNames.dto.UsernameChangeRequest;
import com.omni.main.ValidateUserNames.exceptionHandler.UsernameAlreadyTakenException;
import com.omni.main.ValidateUserNames.exceptionHandler.UsernameHistoryViolationException;
import com.omni.main.ValidateUserNames.service.UsernameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UsernameController.class)
public class Controller_Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsernameService usernameService;

    @Autowired
    private ObjectMapper objectMapper;

    // SUCCESS: Create User
    @Test
    void testCreateUser_Success() throws Exception {
        ApiResponse<Object> response = ApiResponse.success("User Saved successfully");
        Mockito.when(usernameService.createUser(anyString(), anyString(), anyString()))
                .thenReturn(response);

        UserCreateRequest request = new UserCreateRequest();
        request.setUserName("ValidUser1");
        request.setPhoneNumber("+971501234567");
        request.setEmail("validemail@test.com");

        mockMvc.perform(post("/dukan/username/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value("000000"))
                .andExpect(jsonPath("$.data").value("User Saved successfully"));
    }

    // SUCCESS: Change Username
    @Test
    void testChangeUsername_Success() throws Exception {
        ApiResponse<Object> response = ApiResponse.success("User Updated successfully");
        Mockito.when(usernameService.changeUsername(anyLong(), anyString()))
                .thenReturn(response);

        UsernameChangeRequest request = new UsernameChangeRequest();
        request.setUserId(1L);
        request.setNewUsername("NewValidUser1");

        mockMvc.perform(put("/dukan/username/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value("000000"))
                .andExpect(jsonPath("$.data").value("User Updated successfully"));
    }

    // UsernameAlreadyTakenException
    @Test
    void testCreateUser_UsernameAlreadyTakenException() throws Exception {
        Mockito.when(usernameService.createUser(anyString(), anyString(), anyString()))
                .thenThrow(new UsernameAlreadyTakenException("Username already taken!"));

        UserCreateRequest request = new UserCreateRequest();
        request.setUserName("ExistingUser1");
        request.setPhoneNumber("+971501234567");
        request.setEmail("abc@test.com");

        mockMvc.perform(post("/dukan/username/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value("000404"))
                .andExpect(jsonPath("$.status.description").value("Username already taken!"));
    }

    // UsernameHistoryViolationException
    @Test
    void testChangeUsername_HistoryViolationException() throws Exception {
        Mockito.when(usernameService.changeUsername(anyLong(), anyString()))
                .thenThrow(new UsernameHistoryViolationException("Cannot reuse old username"));

        UsernameChangeRequest request = new UsernameChangeRequest();
        request.setUserId(1L);
        request.setNewUsername("OldUser123");

        mockMvc.perform(put("/dukan/username/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value("000404"))
                .andExpect(jsonPath("$.status.description").value("Cannot reuse old username"));
    }

    // Validation Error (MethodArgumentNotValidException)
    @Test
    void testCreateUser_ValidationError() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        request.setUserName(""); // invalid
        request.setPhoneNumber("123"); // invalid
        request.setEmail("wrongemail"); // invalid

        mockMvc.perform(post("/dukan/username/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.code").value("000500"))
                .andExpect(jsonPath("$.status.description").exists());
    }

    // Generic Exception
    @Test
    void testChangeUsername_GenericException() throws Exception {
        Mockito.when(usernameService.changeUsername(anyLong(), anyString()))
                .thenThrow(new RuntimeException("Unexpected error"));

        UsernameChangeRequest request = new UsernameChangeRequest();
        request.setUserId(1L);
        request.setNewUsername("AnyValidUser1");

        mockMvc.perform(put("/dukan/username/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status.code").value("000500"))
                .andExpect(jsonPath("$.status.description").value("Internal server error"));
    }
}
