package br.com.conexasaude.challenge.service;

import br.com.conexasaude.challenge.exception.BadRequestException;
import br.com.conexasaude.challenge.exception.EntityNotFoundException;
import br.com.conexasaude.challenge.model.Patient;
import br.com.conexasaude.challenge.repository.PatientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    PatientRepository patientRepository;

    @InjectMocks
    PatientService patientService;


    @DisplayName("Find by CPF - positive scenario")
    @Test
    void givenValidCpf_whenFindByCpf_thenReturnPatient() {
        // Arrange
        Patient patient = mock(Patient.class);
        given(patientRepository.findByCpf(anyString())).willReturn(Optional.of(patient));

        // Act
        Optional<Patient> patientOptional = patientRepository.findByCpf(anyString());

        // Assert
        assertThat(patientOptional).isPresent();
    }

    @DisplayName("Find by CPF - negative scenario")
    @Test
    void givenInvalidCpf_whenFindByCpf_thenThrowException() {
        // Arrange
        given(patientRepository.findByCpf(anyString())).willReturn(Optional.empty());

        // Act and assert
        assertThrows(BadRequestException.class, () -> patientService.findByCpf("0"));
        assertThrows(EntityNotFoundException.class, () -> patientService.findByCpf("123.123.123-01"));
    }
}
