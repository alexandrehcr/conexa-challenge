package br.com.conexasaude.challenge.model.dto;

import br.com.conexasaude.challenge.constants.ApiMessages;
import br.com.conexasaude.challenge.constants.JsonConstants;
import br.com.conexasaude.challenge.model.Patient;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Future(message = ApiMessages.FUTURE_DATE)
    @JsonProperty(value = JsonConstants.VALUE_LOCAL_DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonConstants.PATTERN_LOCAL_DATE_TIME)
    private LocalDateTime localDateTime;

    @NotBlank(message = ApiMessages.NB_PATIENT)
    @JsonProperty(value = "paciente")
    private Patient patient;
}
