package br.com.conexasaude.challenge.repository;

import br.com.conexasaude.challenge.model.Doctor;
import br.com.conexasaude.challenge.model.dto.JwtLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static br.com.conexasaude.challenge.constants.SecurityConstants.JWT_EXPIRATION_TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class JwtLogRepositoryTest {

    @Autowired
    private JwtLogRepository jwtLogRepository;

    private JwtLog doc1CurrentLog;
    private JwtLog doc1ExpiredLog;
    private JwtLog doc2CurrentLog;


    @BeforeEach
    public void setup() {
        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);
        Doctor doctor2 = new Doctor();
        doctor2.setId(2L);

        Date now = new Date();
        Date expiration = new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME);
        Date revokedAt = new Date(System.currentTimeMillis() + 1000 * 60 * 5);

        doc1CurrentLog = JwtLog.builder().jwt("test.current.token").issuedAt(now).expiration(expiration).user(doctor1).build();
        doc1ExpiredLog = JwtLog.builder().jwt("test.expired.token").issuedAt(now).expiration(expiration).revokedAt(revokedAt).user(doctor1).build();
        doc2CurrentLog = JwtLog.builder().jwt("test.different.token").issuedAt(now).expiration(expiration).user(doctor2).build();
    }

    @DisplayName("Save logs")
    @Test
    public void givenJwtLogs_whenSaving_thenPersistLogs() {
        /* single record saving */
        JwtLog log = jwtLogRepository.save(doc1CurrentLog);

        /* multiple records saving */
        List<JwtLog> logs = jwtLogRepository.saveAll(List.of(doc1ExpiredLog, doc2CurrentLog));

        // Assert
        assertNotNull(log);
        assertEquals(logs.size(), 2);
    }

    @DisplayName("Find all saved logs")
    @Test
    public void givenJwtLogsList_whenFindAll_thenReturnAllLogs() {
        // Arrange
        jwtLogRepository.saveAll(List.of(doc1CurrentLog, doc1ExpiredLog, doc2CurrentLog));

        // Act
        List<JwtLog> logsRecord = jwtLogRepository.findAll();

        // Assert
        assertEquals(logsRecord.size(), 3);
    }

    @DisplayName("Find user's current JWT log")
    @Test
    public void givenUserId_whenFindCurrentLogByUserId_thenReturnCurrentLog() {
        // Arrange
        jwtLogRepository.saveAll(List.of(doc1CurrentLog, doc1ExpiredLog, doc2CurrentLog));

        // Act
        Optional<JwtLog> currentLogFromDoc1 = jwtLogRepository.findCurrentLogByUserId(1L);

        // Assert
        assertThat(currentLogFromDoc1).isPresent();
        assertEquals(currentLogFromDoc1.get(), doc1CurrentLog);
    }
}
