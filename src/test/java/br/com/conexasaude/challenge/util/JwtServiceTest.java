package br.com.conexasaude.challenge.util;

import br.com.conexasaude.challenge.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    JwtService jwtService;
    

    @DisplayName("Create JWT")
    @Test
    public void givenTokenData_whenCreateToken_thenReturnJwt() {

        final Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10);

        // Act
        String jwt = jwtService.createToken("username", new Date(), expiration);

        // Assert
        assertNotNull(jwt);
        assertTrue(jwt.startsWith("ey"));
        assertEquals(3, jwt.split("\\.").length);
    }

    @DisplayName("Extract token from request")
    @Test
    public void givenRequestWithAuthorizationHeader_whenTokenIsExtracted_thenReturnToken() {
        // Arrange
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        given(requestMock.getHeader(HttpHeaders.AUTHORIZATION)).willReturn("Bearer token");

        // Act
        String token = jwtService.extractTokenFromRequest(requestMock);

        // Assert
        assertNotNull(token);
        assertThat(token).isEqualTo("token");
    }
}
