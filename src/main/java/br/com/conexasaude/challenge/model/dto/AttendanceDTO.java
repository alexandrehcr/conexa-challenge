package br.com.conexasaude.challenge.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static br.com.conexasaude.challenge.constants.apimessages.ValidationConstants.FUTURE_DATE;
import static br.com.conexasaude.challenge.constants.apimessages.ValidationConstants.NB_PATIENT;
import static br.com.conexasaude.challenge.constants.json.JsonFields.LOCAL_DATE_TIME;
import static br.com.conexasaude.challenge.constants.json.JsonFields.PATIENT;
import static br.com.conexasaude.challenge.constants.json.JsonPatterns.LOCAL_DATE_TIME_PAT;
import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO {

    @JsonProperty(access = READ_ONLY)
    private Long id;

    @Future(message = FUTURE_DATE)
    @JsonProperty(value = LOCAL_DATE_TIME)
    @JsonFormat(shape = STRING, pattern = LOCAL_DATE_TIME_PAT)
    private LocalDateTime localDateTime;

    @NotNull(message = NB_PATIENT)
    @JsonProperty(value = PATIENT)
    private PatientDTO patient;
}
