package br.com.conexasaude.challenge.security;

import br.com.conexasaude.challenge.constants.ApiMessages;
import br.com.conexasaude.challenge.exception.InvalidJwtException;
import br.com.conexasaude.challenge.security.filter.ExceptionHandlerFilter;
import br.com.conexasaude.challenge.service.JwtLogService;
import br.com.conexasaude.challenge.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@AllArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            String jwt = jwtUtils.extractTokenFromRequest(request);
            if (jwtUtils.isTokenValid(jwt)) {
                jwtUtils.revokeToken(jwt);

            } else {
                throw new InvalidJwtException(ApiMessages.INVALID_JWT_EXCEPTION);
            }
        } catch (InvalidJwtException e) {
            try {
                ExceptionHandlerFilter.setResponse(response, HttpStatus.BAD_REQUEST, e.getMessage());
            } catch (IOException ignored) {

            }
        }
    }
}
