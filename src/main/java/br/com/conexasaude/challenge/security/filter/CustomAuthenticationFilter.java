package br.com.conexasaude.challenge.security.filter;

import br.com.conexasaude.challenge.model.dto.AuthenticationRequest;
import br.com.conexasaude.challenge.service.JwtLogService;
import br.com.conexasaude.challenge.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

import static br.com.conexasaude.challenge.constants.SecurityConstants.BEARER;
import static br.com.conexasaude.challenge.constants.SecurityConstants.JWT_EXPIRATION_TIME;

@AllArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    JwtLogService jwtLogService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AuthenticationRequest authenticationRequest;
        try {
            authenticationRequest = new ObjectMapper().readValue(request.getInputStream(), AuthenticationRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        return daoAuthenticationProvider.authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String username = authResult.getName();
        Date now = new Date();
        Date expiration = new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME);

        // Expires a previous token the user may have before create a new one
        jwtLogService.revokeTokenByUsername(username);
        
        final String jwt = jwtUtils.createToken(username, now, expiration);
        jwtLogService.saveJwtLog(username, jwt, now, expiration);

        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER + jwt);
        response.getWriter().write(jwt);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        throw new BadCredentialsException(failed.getMessage());
    }
}
