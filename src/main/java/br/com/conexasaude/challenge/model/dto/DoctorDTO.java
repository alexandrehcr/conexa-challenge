package br.com.conexasaude.challenge.model.dto;

import br.com.conexasaude.challenge.validation.PasswordMatches;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

import static br.com.conexasaude.challenge.constants.RegexConstants.REGEX_EMAIL;
import static br.com.conexasaude.challenge.constants.RegexConstants.REGEX_TEL;
import static br.com.conexasaude.challenge.constants.apimessages.ValidationConstants.*;
import static br.com.conexasaude.challenge.constants.json.JsonFields.*;
import static br.com.conexasaude.challenge.constants.json.JsonPatterns.LOCAL_DATE_PAT;
import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches(message = PWD_MISMATCH)
public class DoctorDTO {

    @JsonProperty(access = READ_ONLY)
    private Long id;

    @NotBlank(message = NB_NAME)
    @JsonProperty(value = FIRST_NAME)
    private String firstName;

    @NotBlank(message = NB_LAST_NAME)
    @JsonProperty(value = LAST_NAME)
    private String lastName;

    @NotBlank(message = NB_EMAIL)
    @Email(regexp = REGEX_EMAIL, message = INV_EMAIL)
    private String email;

    @NotBlank(message = NB_PWD)
    @JsonProperty(value = PASSWORD, access = WRITE_ONLY)
    private String pwd;

    @JsonProperty(value = PASSWORD_CONFIRMATION, access = WRITE_ONLY)
    private String pwdConfirmation;

    @NotBlank(message = NB_SPECIALTY)
    @JsonProperty(value = SPECIALTY)
    private String specialty;

    @NotNull(message = INV_CPF)
    @CPF(message = INV_CPF)
    private String cpf;

    @Past(message = PAST_DATE)
    @JsonProperty(value = DATE_OF_BIRTH)
    @JsonFormat(shape = STRING, pattern = LOCAL_DATE_PAT)
    private LocalDate dateOfBirth;

    @Pattern(regexp = REGEX_TEL, message = INV_TEL)
    @JsonProperty(value = PHONE_NUMBER)
    private String phoneNumber;
}
