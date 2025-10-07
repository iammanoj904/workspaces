package com.omni.main.ValidateUserNames;

import com.omni.main.ValidateUserNames.dto.ApiResponse;
import com.omni.main.ValidateUserNames.entity.User;
import com.omni.main.ValidateUserNames.entity.UsernameHistory;
import com.omni.main.ValidateUserNames.exceptionHandler.UsernameAlreadyTakenException;
import com.omni.main.ValidateUserNames.exceptionHandler.UsernameHistoryViolationException;
import com.omni.main.ValidateUserNames.repository.UserRepository;
import com.omni.main.ValidateUserNames.repository.UsernameHistoryRepository;
import com.omni.main.ValidateUserNames.service.UsernameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class Service_Test {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UsernameHistoryRepository historyRepository;

   // @InjectMocks
    private UsernameService usernameService;

    private final int historyLimit = 3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usernameService = new UsernameService(userRepository, historyRepository, historyLimit);
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.findByUsername("TestUser")).thenReturn(Optional.empty());

        ApiResponse<?> response = usernameService.createUser("TestUser", "+971501234567", "abc@test.com");

        assertEquals("000000", response.getStatus().getCode());
        assertEquals("User Saved successfully", response.getData());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_ThrowsUsernameAlreadyTakenException() {
        when(userRepository.findByUsername("ExistingUser"))
                .thenReturn(Optional.of(new User()));

        assertThrows(UsernameAlreadyTakenException.class, () ->
                usernameService.createUser("ExistingUser", "+971501234567", "abc@test.com"));
    }

    @Test
    void testChangeUsername_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("OldUser");

        when(userRepository.findByUsername("NewUser")).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Page<UsernameHistory> emptyHistory = new PageImpl<>(List.of());
        when(historyRepository.findByUserIdOrderByChangedAtDesc(eq(userId), any(PageRequest.class)))
                .thenReturn(emptyHistory);

        ApiResponse<?> response = usernameService.changeUsername(userId, "NewUser");

        assertEquals("000000", response.getStatus().getCode());
        assertEquals("User Updated successfully", response.getData());
        verify(historyRepository, times(1)).save(any(UsernameHistory.class));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testChangeUsername_NoDataFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.findByUsername("NewUser")).thenReturn(Optional.empty());

        assertThrows(UsernameHistoryViolationException.class, () ->
                usernameService.changeUsername(1L, "NewUser"));
    }

    @Test
    void testChangeUsername_AlreadyUpToDate() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("SameUser");

        when(userRepository.findByUsername("SameUser")).thenReturn(Optional.of(user));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ApiResponse<?> response = usernameService.changeUsername(userId, "SameUser");
        assertEquals("000000", response.getStatus().getCode());
        assertTrue(response.getData().toString().contains("already up to date"));
    }

    @Test
    void testChangeUsername_ReuseOldUsername_ThrowsException() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("OldUser");

        UsernameHistory hist = new UsernameHistory();
        hist.setOldUsername("NewUser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("NewUser")).thenReturn(Optional.empty());
        when(historyRepository.findByUserIdOrderByChangedAtDesc(eq(userId), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(hist)));

        assertThrows(UsernameHistoryViolationException.class, () ->
                usernameService.changeUsername(userId, "NewUser"));
    }

}
