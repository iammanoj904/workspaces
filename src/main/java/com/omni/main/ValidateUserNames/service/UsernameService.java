package com.omni.main.ValidateUserNames.service;

import com.omni.main.ValidateUserNames.dto.ApiResponse;
import com.omni.main.ValidateUserNames.entity.User;
import com.omni.main.ValidateUserNames.entity.UsernameHistory;
import com.omni.main.ValidateUserNames.exceptionHandler.UsernameAlreadyTakenException;
import com.omni.main.ValidateUserNames.exceptionHandler.UsernameHistoryViolationException;
import com.omni.main.ValidateUserNames.repository.UserRepository;
import com.omni.main.ValidateUserNames.repository.UsernameHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsernameService implements IUsernameService {

    private final UserRepository userRepository;
    private final UsernameHistoryRepository historyRepository;
    private final int historyLimit;

    public UsernameService(UserRepository userRepository,
                           UsernameHistoryRepository historyRepository,
                           @Value("${username.history.limit}") int historyLimit) {
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
        this.historyLimit = historyLimit;
    }

    // Create user
    public ApiResponse<Object> createUser(String username,String phoneNumber, String email) {
        userRepository.findByUsername(username)
                .ifPresent(u -> { throw new UsernameAlreadyTakenException("Username already taken!"); });

        User user = new User();
        user.setUsername(username);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        userRepository.save(user);

        return ApiResponse.success("User Saved successfully");
    }

    // Change username
    @Transactional
    public ApiResponse<Object> changeUsername(Long userId, String newUsername) {
        userRepository.findByUsername(newUsername)
                .filter(u -> !u.getId().equals(userId))
                .ifPresent(u -> { throw new UsernameAlreadyTakenException("Username already taken!"); });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameHistoryViolationException("No Data Found"));

        // Prevent duplicate insert if username is same
        if (user.getUsername().equalsIgnoreCase(newUsername)) {
            return ApiResponse.success("Username is already up to date — no change made");
        }

        List<UsernameHistory> lastUsernames = historyRepository
                .findByUserIdOrderByChangedAtDesc(userId, PageRequest.of(0, historyLimit))
                .getContent();

        boolean reused = lastUsernames.stream()
                .anyMatch(u -> u.getOldUsername().equalsIgnoreCase(newUsername));

        if (reused) {
            throw new UsernameHistoryViolationException(
                    "Cannot reuse your last " + historyLimit + " usernames."
            );
        }
//
        // Save history once
        UsernameHistory history = new UsernameHistory();
        history.setUser(user);
        history.setOldUsername(user.getUsername());
        historyRepository.save(history);

        // Update username
        user.setUsername(newUsername);
        userRepository.save(user);

        return ApiResponse.success("User Updated successfully");
    }


    //new method to store old userName as per application properties value
   /* @Transactional
    public ApiResponse<?> changeUsername(Long userId, String newUsername) {
        userRepository.findByUsername(newUsername)
                .filter(u -> !u.getId().equals(userId))
                .ifPresent(u -> { throw new UsernameAlreadyTakenException("Username already taken!"); });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameHistoryViolationException("No Data Found"));

        if (user.getUsername().equalsIgnoreCase(newUsername)) {
            return ApiResponse.success("Username is already up to date — no change made");
        }

        // Get last usernames (limit)
        List<UsernameHistory> lastUsernames = historyRepository
                .findByUserIdOrderByChangedAtDesc(userId, PageRequest.of(0, historyLimit))
                .getContent();

        boolean reused = lastUsernames.stream()
                .anyMatch(u -> u.getOldUsername().equalsIgnoreCase(newUsername));

        if (reused) {
            throw new UsernameHistoryViolationException(
                    "Cannot reuse your last " + historyLimit + " usernames."
            );
        }

        // Save new history entry
        UsernameHistory history = new UsernameHistory();
        history.setUser(user);
        history.setOldUsername(user.getUsername());
        historyRepository.save(history);

        // Cleanup old history beyond limit
        cleanupOldHistory(userId);

        // Update username
        user.setUsername(newUsername);
        userRepository.save(user);

        return ApiResponse.success("User Updated successfully");
    }

    //helper method for changeUserName
    private void cleanupOldHistory(Long userId) {
        // Fetch all usernames sorted by newest first
        List<UsernameHistory> allHistories = historyRepository
                .findByUserIdOrderByChangedAtDesc(userId, PageRequest.of(0, Integer.MAX_VALUE))
                .getContent();

        // If more than limit, delete the older ones
        if (allHistories.size() > historyLimit) {
            List<UsernameHistory> toDelete = allHistories.subList(historyLimit, allHistories.size());
            historyRepository.deleteAll(toDelete);
        }
    }*/


}