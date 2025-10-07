package com.omni.main.ValidateUserNames;

import com.omni.main.ValidateUserNames.entity.User;
import com.omni.main.ValidateUserNames.entity.UsernameHistory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class Entity_Test {

    @Test
    void testOnCreateSetsTimestamps() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPhoneNumber("1234567890");
        user.setHistories(Collections.emptyList());

        // Call lifecycle manually
        user.onCreate();

        assertNotNull(user.getCreatedAt(), "createdAt should not be null after onCreate");
        assertNotNull(user.getUpdatedAt(), "updatedAt should not be null after onCreate");
        assertEquals(user.getCreatedAt(), user.getUpdatedAt(),
                "createdAt and updatedAt should be same on creation");
    }

    @Test
    void testOnUpdateSetsUpdatedAt() throws InterruptedException {
        User user = new User();
        user.onCreate(); // first call to set initial timestamps

        LocalDateTime oldUpdatedAt = user.getUpdatedAt();
        Thread.sleep(5); // ensure time difference
        user.onUpdate();

        assertTrue(user.getUpdatedAt().isAfter(oldUpdatedAt),
                "updatedAt should be refreshed after onUpdate()");
    }

    @Test
    void testGetterAndSetter() {
        User user = new User();
        user.setId(1L);
        user.setUsername("demoUser");
        user.setEmail("demo@example.com");
        user.setPhoneNumber("9999999999");

        assertEquals(1L, user.getId());
        assertEquals("demoUser", user.getUsername());
        assertEquals("demo@example.com", user.getEmail());
        assertEquals("9999999999", user.getPhoneNumber());
    }


    //UserNameHistoryEntity
    @Test
    void testOnCreateSetsChangedAt() {
        UsernameHistory history = new UsernameHistory();
        history.setOldUsername("OldUser");

        User user = new User();
        user.setId(1L);
        user.setUsername("TestUser");
        history.setUser(user);

        // Call lifecycle manually
        history.onCreate();

        assertNotNull(history.getChangedAt(), "changedAt should not be null after onCreate");
        assertTrue(history.getChangedAt().isBefore(LocalDateTime.now().plusSeconds(1)),
                "changedAt should be set to a time before now");
    }

    @Test
    void testGetterAndSetterr() {
        UsernameHistory history = new UsernameHistory();

        User user = new User();
        user.setId(10L);
        user.setUsername("User10");

        history.setId(100L);
        history.setUser(user);
        history.setOldUsername("PreviousUser");

        assertEquals(100L, history.getId());
        assertEquals(user, history.getUser());
        assertEquals("PreviousUser", history.getOldUsername());
    }
}
