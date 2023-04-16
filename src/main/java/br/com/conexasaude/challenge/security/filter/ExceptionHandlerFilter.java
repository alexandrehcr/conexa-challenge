package br.com.conexasaude.challenge.security.filter;

import br.com.conexasaude.challenge.constants.ApiMessages;
import br.com.conexasaude.challenge.constants.JsonConstants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static br.com.conexasaude.challenge.constants.ApiMessages.*;
import static br.com.conexasaude.challenge.constants.SecurityConstants.LOGIN_PATH;
import static br.com.conexasaude.challenge.constants.SecurityConstants.REGISTRATION_PATH;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if ((request.getRequestURI().equals(LOGIN_PATH) || request.getRequestURI().equals(REGISTRATION_PATH)) && !request.getMethod().equals(HttpMethod.POST.toString())) {
            setResponse(response, HttpStatus.METHOD_NOT_ALLOWED, String.format(ApiMessages.METHOD_NOT_ALLOWED, "POST", request.getMethod()));
            return;
        }

        try {
            filterChain.doFilter(request, response);

        } catch (AuthenticationException | BadCredentialsException ex) {
            setResponse(response, HttpStatus.UNAUTHORIZED, INVALID_USERNAME_PASSWORD);

        } catch (UnsupportedJwtException ex) {
            setResponse(response, HttpStatus.UNAUTHORIZED, UNSUPPORTED_JWT_EXCEPTION);

        } catch (MalformedJwtException ex) {
            setResponse(response, HttpStatus.UNAUTHORIZED, MALFORMED_JWT_EXCEPTION);

        } catch (SignatureException ex) {
            setResponse(response, HttpStatus.UNAUTHORIZED, SIGNATURE_EXCEPTION);

        } catch (IllegalArgumentException ex) {
            setResponse(response, HttpStatus.BAD_REQUEST, ILLEGAL_ARGUMENT_EXCEPTION);

        }
    }

    public static void setResponse (HttpServletResponse response, HttpStatus httpStatus, String message) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject json = new JsonObject();
        json.addProperty("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern(JsonConstants.PATTERN_LOCAL_DATE_TIME)));
        json.addProperty("code", httpStatus.value());
        json.addProperty("status", httpStatus.name());
        json.addProperty("message", message);

        response.getWriter().write(new Gson().toJson(json));
        response.getWriter().flush();
    }
}