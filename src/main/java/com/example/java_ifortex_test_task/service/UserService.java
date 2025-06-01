package com.example.java_ifortex_test_task.service;

import com.example.java_ifortex_test_task.dto.UserResponseDTO;
import com.example.java_ifortex_test_task.entity.User;
import com.example.java_ifortex_test_task.mapper.UserMapper;
import com.example.java_ifortex_test_task.repository.SessionRepository;
import com.example.java_ifortex_test_task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type User service.
 * <p>
 * This service class provides operations related to users, including fetching user
 * information based on their session data. It acts as a bridge between the user repository
 * and controllers, allowing for data manipulation and retrieval.
 * </p>
 *
 * @author razlivinsky
 * @since 30.05.2025
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final UserMapper userMapper;

    /**
     * Gets the user with the most sessions.
     *
     * <p>
     * This method retrieves the user from the repository who has the highest number of sessions.
     * It uses the {@link UserRepository#getUserWithMostSessions()} method to perform the retrieval
     * and maps the user entity to a {@link UserResponseDTO} using the {@link UserMapper}.
     * </p>
     *
     * @return the user with the most sessions as a {@link UserResponseDTO}.
     *         Returns null if no users are found.
     */
    public UserResponseDTO getUserWithMostSessions() {
        User user = userRepository.getUserWithMostSessions();
        return userMapper.toDto(user);
    }

    /**
     * Gets users with at least one mobile session.
     *
     * <p>
     * This method retrieves a list of users who have at least one mobile session
     * by calling the {@link UserRepository#getUsersWithAtLeastOneMobileSession(int)} method
     * with device type 2. It then maps the list of user entities to a list of
     * {@link UserResponseDTO} using the {@link UserMapper}.
     * </p>
     *
     * @return a list of users with at least one mobile session as {@link UserResponseDTO} objects.
     *         The list may be empty if no such users exist.
     */
    public List<UserResponseDTO> getUsersWithAtLeastOneMobileSession() {
        List<User> users = userRepository.getUsersWithAtLeastOneMobileSession(1);
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}