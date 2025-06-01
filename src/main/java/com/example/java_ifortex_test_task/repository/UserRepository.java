package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserRepository interface provides methods for managing user authentication and session data.
 * It extends JpaRepository to leverage CRUD operations on User entities.
 *
 * @author razlivinsky
 * @since 30.05.2025
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Retrieves the user who has the maximum number of sessions.
     *
     * <p>This method executes a native SQL query that joins the users and sessions tables,
     * groups the results by user ID, and orders them by the session count in descending order.
     * It limits the result to 1, ensuring that only the user with the most sessions is returned.</p>
     *
     * @return the user with the most sessions, or null if no users have sessions.
     */
    @Query(value = "SELECT u.* " +
            "FROM users u " +
            "INNER JOIN sessions s ON u.id = s.user_id " +
            "GROUP BY u.id " +
            "ORDER BY COUNT(s.id) DESC " +
            "LIMIT 1", nativeQuery = true)
    User getUserWithMostSessions();

    /**
     * Retrieves a list of users who have at least one session on a specified device type.
     *
     * <p>This method executes a native SQL query that selects users whose IDs are found in
     * the sessions table for the given device type. The results are ordered by the most recent
     * session start time for that device type.</p>
     *
     * @param deviceType the type of device (represented as an integer) for filtering sessions.
     *                   Valid values are defined by the application context (e.g., 1 for mobile, 2 for desktop).
     * @return a list of users with at least one session on the specified device type.
     *         The list may be empty if no users match the criteria.
     */
    @Query(value = "SELECT u.* " +
            "FROM users u " +
            "WHERE u.id IN (SELECT s.user_id FROM sessions s WHERE s.device_type = :deviceType) " +
            "ORDER BY (SELECT MAX(s2.started_at_utc) " +
            "          FROM sessions s2 " +
            "          WHERE s2.user_id = u.id AND s2.device_type = :deviceType) DESC", nativeQuery = true)
    List<User> getUsersWithAtLeastOneMobileSession(@Param("deviceType") int deviceType);
}
