package br.com.conexasaude.challenge.security;

import br.com.conexasaude.challenge.exception.InvalidJwtException;
import br.com.conexasaude.challenge.security.filter.ExceptionHandlerFilter;
import br.com.conexasaude.challenge.service.JwtLogService;
import br.com.conexasaude.challenge.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;

import static br.com.conexasaude.challenge.constants.apimessages.ExceptionMessages.INVALID_JWT_EXCEPTION;

@NoArgsConstructor
@AllArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    JwtService jwtService;
    JwtLogService jwtLogService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            String jwt = jwtService.extractTokenFromRequest(request);
            if (jwtLogService.isTokenValid(jwt)) {
                jwtLogService.revokeToken(jwt);

            } else {
                throw new InvalidJwtException(INVALID_JWT_EXCEPTION);
            }
        } catch (InvalidJwtException e) {
            try {
                ExceptionHandlerFilter.setResponse(response, HttpStatus.BAD_REQUEST, e.getMessage());
            } catch (IOException ignored) {

            }
        }
    }
}
