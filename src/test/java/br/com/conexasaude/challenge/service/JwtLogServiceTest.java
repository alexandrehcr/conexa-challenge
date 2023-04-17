package br.com.conexasaude.challenge.service;

import br.com.conexasaude.challenge.model.Doctor;
import br.com.conexasaude.challenge.model.dto.JwtLog;
import br.com.conexasaude.challenge.repository.DoctorRepository;
import br.com.conexasaude.challenge.repository.JwtLogRepository;
import br.com.conexasaude.challenge.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.Optional;

import static br.com.conexasaude.challenge.constants.SecurityConstants.JWT_EXPIRATION_TIME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtLogServiceTest {

    @Mock
    JwtLogRepository jwtLogRepository;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    DoctorRepository doctorRepository;

    @InjectMocks
    JwtLogServiceImpl jwtLogService;

    JwtLog jwtLog;
    final String jwt = "test.jwt.token";
    final Date expiration = new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME);


    @BeforeEach
    public void setup() {
        jwtLog = JwtLog.builder().jwt(jwt).issuedAt(new Date()).expiration(expiration).user(mock(Doctor.class)).build();

        lenient().when(jwtUtils.extractUsername(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        lenient().when(doctorRepository.findByEmail(anyString())).thenReturn(Optional.of(mock(Doctor.class)));
    }

    @DisplayName("Create log")
    @Test
    public void givenTokenData_whenSaveLog_thenRegisterSave() {
        // Arrange
        given(jwtLogRepository.save(any(JwtLog.class))).willAnswer(invocation -> invocation.getArgument(0));

        // Act
        JwtLog savedLog = jwtLogService.saveJwtLog("username", jwt, new Date(), expiration);

        // Assert
        assertNotNull(savedLog);
        assertNull(savedLog.getRevokedAt());
        assertEquals(jwtLog.getExpiration(), expiration);
        assertEquals(jwt, savedLog.getJwt());
    }


    @DisplayName("Revoke token when token exists")
    @Test
    public void givenUserId_whenTokenIsRevoked_thenUpdateRevokeAtField() {
        // Arrange
        when(jwtLogRepository.findCurrentLogByUserId(anyLong())).thenReturn(Optional.of(jwtLog));

        // Act
        jwtLogService.revokeTokenByUserId(anyLong());

        // Assert
        assertNotNull(jwtLog.getRevokedAt());
    }


    // Valid token and user has log
    @DisplayName("Check token validity - positive scenario")
    @Test
    public void givenValidToken_whenTokenValidityIsVerified_thenReturnTrue() {
        //Arrange
        given(jwtLogRepository.findCurrentLogByUserId(anyLong())).willReturn(Optional.of(jwtLog));

        // Act
        boolean expectTrue = jwtLogService.isTokenValid(jwt);

        // Assert
        assertTrue(expectTrue);
    }


    // Invalid token or no log for the user
    @DisplayName("Check token validity - negative scenario")
    @Test
    public void givenInvalidToken_whenTokenIsVerified_thenReturnFalse() {
        /* invalid token */
        // Arrange
        given(jwtLogRepository.findCurrentLogByUserId(anyLong())).willReturn(Optional.of(jwtLog));

        // Act
        boolean result = jwtLogService.isTokenValid(jwt + "x");

        // Assert
        assertFalse(result);


        /* user has no log */
        // Arrange
        given(jwtLogRepository.findCurrentLogByUserId(anyLong())).willReturn(Optional.empty());

        // Act
        result = jwtLogService.isTokenValid(jwt);

        // Assert
        assertFalse(result);
    }


    @DisplayName("Get data from the authenticated user")
    @Test
    public void givenAuthenticationObject_whenProvidedValidSupplier_thenReturnRequestedField() {
        // Arrange
        Authentication authentication = mock(UsernamePasswordAuthenticationToken.class);
        String email = "test@email.com";
        Doctor doctor = new Doctor();
        doctor.setEmail(email);
        doctor.setId(1L);

        given(authentication.getName()).willReturn(email);
        given(doctorRepository.findByEmail(email)).willReturn(Optional.of(doctor));

        // Act
        String emailResult = jwtLogService.getFromUser(authentication, Doctor::getEmail);
        Long idResult = jwtLogService.getFromUser(authentication, Doctor::getId);

        // Assert
        assertEquals(email, emailResult);
        assertEquals(1L, idResult);
    }
}
