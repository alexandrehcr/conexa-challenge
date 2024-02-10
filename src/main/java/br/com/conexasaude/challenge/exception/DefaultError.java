package br.com.conexasaude.challenge.exception;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static br.com.conexasaude.challenge.constants.json.JsonPatterns.LOCAL_DATE_TIME_PAT;

@Getter
@Setter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class DefaultError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = LOCAL_DATE_TIME_PAT)
    private LocalDateTime timestamp;
    private int code;
    private String status;
    private List<String> messages;


    private DefaultError(HttpStatus status) {
        timestamp = LocalDateTime.now();
        this.code = status.value();
        this.status = status.getReasonPhrase();
    }

    public DefaultError(HttpStatus status, String message) {
        this(status);
        this.messages = Arrays.asList(message);
    }

    public DefaultError(HttpStatus status, List<String> messages) {
        this(status);
        this.messages = messages;
    }
}
