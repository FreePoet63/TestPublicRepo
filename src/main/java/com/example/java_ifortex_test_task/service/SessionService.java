package com.example.java_ifortex_test_task.service;

import com.example.java_ifortex_test_task.dto.SessionResponseDTO;
import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.mapper.SessionMapper;
import com.example.java_ifortex_test_task.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Session service.
 * <p>
 * This service class provides operations related to session management,
 * including retrieving specific session data based on various criteria.
 * It acts as a bridge between the session repository and controllers,
 * enabling data retrieval and transformation into suitable response formats.
 * </p>
 *
 * @author razlivinsky
 * @since 30.05.2025
 */
@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;

    /**
     * Retrieves the first desktop session.
     *
     * <p>
     * This method calls the {@link SessionRepository#getFirstDesktopSession(int)}
     * with a device type parameter of 2 to retrieve the first session for a desktop device type.
     * If a matching session is found, it maps the retrieved session data to a
     * {@link SessionResponseDTO} object using the appropriate mapping logic.
     * </p>
     *
     * @return the first desktop session as a {@link SessionResponseDTO}.
     *         Returns null if no desktop session is found or if the result set is empty.
     */
    public SessionResponseDTO getFirstDesktopSession() {
        List<Object[]> result = sessionRepository.getFirstDesktopSession(2);
        Object[] row = result.get(0);
        SessionResponseDTO dto = new SessionResponseDTO();
        dto.setId(((Number) row[0]).longValue());
        dto.setStartedAtUtc(((Timestamp) row[1]).toLocalDateTime());
        dto.setEndedAtUtc(((Timestamp) row[2]).toLocalDateTime());
        dto.setDeviceType(DeviceType.fromCode(((Number) row[3]).intValue()));
        dto.setUserId(((Number) row[4]).longValue());
        dto.setUserFullName((String) row[5]);
        return dto;
    }

    /**
     * Retrieves sessions from active users that ended before January 1, 2025.
     *
     * <p>
     * This method utilizes the {@link SessionRepository#getSessionsFromActiveUsersEndedBefore2025(LocalDateTime)}
     * to fetch raw session data associated with active users. The data is filtered to include only
     * those sessions that ended before the specified cut-off date. The retrieved list of session
     * entities (in raw form) is then transformed into a list of {@link SessionResponseDTO} objects
     * using the mapping logic defined in this method.
     * </p>
     *
     * @return a list of {@link SessionResponseDTO} objects representing sessions from active users
     *         that ended before January 1, 2025. The list may be empty if no sessions match the criteria.
     */
    public List<SessionResponseDTO> getSessionsFromActiveUsersEndedBefore2025() {
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 1, 0, 0);
        List<Object[]> rawResults = sessionRepository.getSessionsFromActiveUsersEndedBefore2025(endDate);
        return rawResults.stream()
                .map(row -> {
                    SessionResponseDTO dto = new SessionResponseDTO();
                    dto.setId(((Number) row[0]).longValue());
                    dto.setStartedAtUtc(((Timestamp) row[1]).toLocalDateTime());
                    dto.setEndedAtUtc(((Timestamp) row[2]).toLocalDateTime());
                    dto.setDeviceType(DeviceType.fromCode(((Number) row[3]).intValue()));
                    dto.setUserId(((Number) row[4]).longValue());
                    dto.setUserFullName((String) row[5]);
                    return dto;
                })
                .toList();
    }
}