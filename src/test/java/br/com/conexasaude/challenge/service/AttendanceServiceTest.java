package br.com.conexasaude.challenge.service;

import br.com.conexasaude.challenge.constants.ApiMessages;
import br.com.conexasaude.challenge.exception.AttendanceException;
import br.com.conexasaude.challenge.model.Attendance;
import br.com.conexasaude.challenge.repository.AttendanceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class AttendanceServiceTest {

    @Mock
    AttendanceRepository attendanceRepository;

    @InjectMocks
    AttendanceService attendanceService;


    // There's no unique constraint violation
    @DisplayName("Save attendance - positive scenario")
    @Test
    public void givenAttendance_whenSaveAttendance_thenReturnAttendance() {
        // Arrange
        Attendance attendance = mock(Attendance.class);
        given(attendanceRepository.save(attendance)).willReturn(attendance);

        // Act
        Attendance savedAttendance = attendanceService.save(attendance);

        // Assert
        assertThat(savedAttendance).isNotNull();
    }

    // Unique constraint violation
    @DisplayName("Save attendance - negative scenario")
    @Test
    public void givenConflictingAttendance_whenSaveAttendance_thenThrowException() {
        // Arrange
        Attendance attendance = mock(Attendance.class);
        doThrow(DataIntegrityViolationException.class).when(attendanceRepository).save(attendance);

        // Act and assert
        assertThrows(AttendanceException.class, () -> attendanceService.save(attendance), ApiMessages.ATTENDANCE_UNIQUE_CONSTRAINT_VIOLATION);
    }
}
