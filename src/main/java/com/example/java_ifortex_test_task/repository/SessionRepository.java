package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The interface for managing session data in the application.
 * <p>
 * This repository extends {@link JpaRepository} to provide CRUD operations for {@link Session} entities.</p>
 *
 * @author razlivinsky
 * @since 30.05.2025
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    /**
     * Retrieves the first session for a specified device type.
     *
     * <p>
     * This method executes a native SQL query to select the first session record from the
     * sessions table that matches the given device type. The results are ordered by the
     * session start time in ascending order, and the result is limited to only one session.
     * </p>
     *
     * @param deviceType the type of device to filter sessions (e.g., 1 for mobile, 2 for desktop).
     * @return an array of objects representing the first session for the specified device type,
     *         or null if no session matches the criteria. The object array contains the following
     *         elements: [id, started_at_utc, ended_at_utc, device_type, user_id, full_name].
     */
    @Query(value = "SELECT " +
            "    s.id, " +
            "    s.started_at_utc, " +
            "    s.ended_at_utc, " +
            "    s.device_type, " +
            "    s.user_id, " +
            "    u.first_name || ' ' || u.last_name AS full_name " +
            "FROM " +
            "    sessions s " +
            "JOIN " +
            "    users u ON s.user_id = u.id " +
            "WHERE " +
            "    s.device_type = :deviceType " +
            "ORDER BY " +
            "    s.started_at_utc ASC " +
            "LIMIT 1",
            nativeQuery = true)
    List<Object[]> getFirstDesktopSession(@Param("deviceType") int deviceType);

    /**
     * Retrieves a list of sessions from active users that ended before the specified date.
     *
     * <p>
     * This method executes a native SQL query that joins the sessions and users tables to find
     * sessions of users who are not marked as deleted. The results are filtered to include only
     * sessions that have ended before the specified end date. The results are ordered by the
     * session start time in descending order.
     * </p>
     *
     * @param endDate the end date to filter sessions that ended before this date.
     * @return a list of session data from active users that ended before the specified end date.
     *         Each entry in the list is represented as an array of objects, where the elements
     *         correspond to the selected columns: [id, started_at_utc, ended_at_utc, device_type,
     *         user_id, full_name]. The list may be empty if no sessions match the criteria.
     */
    @Query(value = "SELECT " +
            "    s.id, " +
            "    s.started_at_utc, " +
            "    s.ended_at_utc, " +
            "    s.device_type, " +
            "    s.user_id, " +
            "    u.first_name || ' ' || u.last_name AS full_name " +
            "FROM " +
            "    sessions s " +
            "JOIN " +
            "    users u ON s.user_id = u.id " +
            "WHERE " +
            "    s.device_type IN (1, 2) " +
            "    AND u.deleted = false " +
            "    AND s.ended_at_utc < :endDate " +
            "ORDER BY " +
            "    s.started_at_utc DESC",
            nativeQuery = true)
    List<Object[]> getSessionsFromActiveUsersEndedBefore2025(@Param("endDate") LocalDateTime endDate);
}