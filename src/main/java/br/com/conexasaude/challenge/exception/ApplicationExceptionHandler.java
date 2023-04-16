package br.com.conexasaude.challenge.exception;

import br.com.conexasaude.challenge.constants.ApiMessages;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new DefaultError(httpStatus, errors), httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        DefaultError error = new DefaultError(httpStatus, ex.getMostSpecificCause().getMessage());
        return new ResponseEntity<>(error, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
        String message = String.format(ApiMessages.METHOD_NOT_ALLOWED, ex.getSupportedHttpMethods(), ex.getMethod());
        return new ResponseEntity<>(new DefaultError(httpStatus, message), httpStatus);
    }

    @ExceptionHandler({BadRequestException.class, AttendanceException.class})
    public ResponseEntity<Object> handleBadRequests(RuntimeException ex){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new DefaultError(httpStatus, ex.getMessage()), httpStatus);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleBadCredentials(RuntimeException ex){
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(new DefaultError(httpStatus, ex.getMessage()), httpStatus);
    }

    @ExceptionHandler({EntityNotFoundException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleEntityNotFound(RuntimeException ex) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(new DefaultError(httpStatus, ex.getMessage()), httpStatus);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleSQLIntegrityConstraintViolation(DataIntegrityViolationException ex, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        return new ResponseEntity<>(new DefaultError(httpStatus, ex.getMessage()), httpStatus);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> genericHandler(Exception ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(new DefaultError(httpStatus, ex.getMessage()), httpStatus);
    }
}
