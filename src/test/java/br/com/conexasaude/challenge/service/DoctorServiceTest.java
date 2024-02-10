package br.com.conexasaude.challenge.service;

import br.com.conexasaude.challenge.constants.apimessages.UniqueConstraintViolationMessages;
import br.com.conexasaude.challenge.model.Attendance;
import br.com.conexasaude.challenge.model.Doctor;
import br.com.conexasaude.challenge.model.Patient;
import br.com.conexasaude.challenge.model.dto.AttendanceDTO;
import br.com.conexasaude.challenge.model.dto.DoctorDTO;
import br.com.conexasaude.challenge.model.dto.PatientDTO;
import br.com.conexasaude.challenge.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    DoctorRepository doctorRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    PatientService patientService;

    @Mock
    AttendanceService attendanceService;

    @InjectMocks
    DoctorService doctorService;

    DoctorDTO registerDTO;


    @BeforeEach
    void setup() {
        // This object cannot be mocked because its field are going to be used from a static and a private method.
        registerDTO = new DoctorDTO();
        registerDTO.setCpf("0");
        registerDTO.setPhoneNumber("0");
        registerDTO.setEmail("test@email.com");
    }


    // At this point, any validation exception would have been caught by the application exception handler
    // so there's no need to check invalid cpf, email, password, etc.
    @DisplayName("Doctor's registration test - positive scenario")
    @Test
    void givenValidRegistrationData_whenSaveDoctor_thenReturnDTOFromSavedDoctor() {
        // Arrange
        given(doctorRepository.existsByEmail(registerDTO.getEmail())).willReturn(false);
        given(doctorRepository.existsByCpf(registerDTO.getCpf())).willReturn(false);
        given(doctorRepository.save(any(Doctor.class))).willAnswer(invocation -> invocation.getArgument(0));

        // Act
        DoctorDTO dtoFromSavedDoctor = doctorService.register(registerDTO);

        // Assert
        assertThat(dtoFromSavedDoctor.getEmail()).isEqualTo(registerDTO.getEmail());
        assertThat(dtoFromSavedDoctor.getCpf()).isEqualTo(registerDTO.getCpf());
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }


    @DisplayName("Doctor's registration test rest - negative scenario")
    @Test
    void givenInvalidRegistrationData_whenSaveDoctor_thenThrowException() {
        /* Only email is already registered */
        // Arrange
        given(doctorRepository.existsByEmail(registerDTO.getEmail())).willReturn(true);
        given(doctorRepository.existsByCpf(registerDTO.getCpf())).willReturn(false);

        // Act and assert
        assertThrows(DataIntegrityViolationException.class, () -> doctorService.register(registerDTO), UniqueConstraintViolationMessages.EMAIL_UNIQUE_CONSTRAINT_VIOLATION);


        /* Only CPF is already registered */
        // Arrange
        given(doctorRepository.existsByEmail(registerDTO.getEmail())).willReturn(false);
        given(doctorRepository.existsByCpf(registerDTO.getCpf())).willReturn(true);

        // Act and assert
        assertThrows(DataIntegrityViolationException.class, () -> doctorService.register(registerDTO), UniqueConstraintViolationMessages.CPF_UNIQUE_CONSTRAINT_VIOLATION);


        /* Both email and CPF are already registered */
        // Arrange
        given(doctorRepository.existsByEmail(registerDTO.getEmail())).willReturn(true);
        given(doctorRepository.existsByCpf(registerDTO.getCpf())).willReturn(true);

        // Act and assert
        assertThrows(DataIntegrityViolationException.class, () -> doctorService.register(registerDTO), UniqueConstraintViolationMessages.EMAIL_AND_CPF_UNIQUE_CONSTRAINT_VIOLATION);

        // Assert that any invalid object was saved
        verify(doctorRepository, never()).save(any(Doctor.class));
    }


    @DisplayName("Attendance's registration test")
    @Test
    void givenAttendanceDTO_whenSaveAttendance_thenReturnDTOFromSavedAttendance() {
        // Arrange
        Doctor doctor = mock(Doctor.class);

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setCpf("0");

        Patient patient = new Patient();
        patient.setCpf(patientDTO.getCpf());

        AttendanceDTO attendanceDTO = new AttendanceDTO();
        attendanceDTO.setLocalDateTime(LocalDateTime.now());
        attendanceDTO.setPatient(patientDTO);

        Authentication authentication = new UsernamePasswordAuthenticationToken("principal", "credentials", new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        given(patientService.findByCpf(anyString())).willReturn(patient);
        given(doctorRepository.findByEmail(anyString())).willReturn(Optional.of(doctor));
        given(attendanceService.save(any(Attendance.class))).willAnswer(invocation -> invocation.getArgument(0));

        // Act
        AttendanceDTO savedAttendanceDTO = doctorService.scheduleAttendance(attendanceDTO);

        // Assert
        assertThat(savedAttendanceDTO).isNotNull();
        assertThat(savedAttendanceDTO.getPatient().getCpf()).isEqualTo("0");
        assertThat(savedAttendanceDTO.getLocalDateTime()).isEqualTo(attendanceDTO.getLocalDateTime());
        verify(attendanceService, times(1)).save(any(Attendance.class));
    }
}
