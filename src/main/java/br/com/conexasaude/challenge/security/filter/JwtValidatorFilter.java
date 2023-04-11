package br.com.conexasaude.challenge.security.filter;

import br.com.conexasaude.challenge.constants.ApiMessages;
import br.com.conexasaude.challenge.service.JwtLogService;
import br.com.conexasaude.challenge.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static br.com.conexasaude.challenge.constants.SecurityConstants.BEARER;
import static br.com.conexasaude.challenge.constants.SecurityConstants.HOME_PATH;

@AllArgsConstructor
public class JwtValidatorFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER) || request.getRequestURI().equals(HOME_PATH)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = header.replace(BEARER, "");

        if (!jwtUtils.isTokenValid(token)) {
            ExceptionHandlerFilter.setResponse(response, HttpStatus.BAD_REQUEST, ApiMessages.INVALID_JWT_EXCEPTION);
            return;
        } else {
            // Token is also validated when parsing claims
            String username = jwtUtils.extractUsername(token);
            Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(username, null, Arrays.asList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
