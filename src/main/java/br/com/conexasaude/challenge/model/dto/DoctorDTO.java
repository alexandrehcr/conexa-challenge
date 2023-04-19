package br.com.conexasaude.challenge.model.dto;

import br.com.conexasaude.challenge.constants.ApiMessages;
import br.com.conexasaude.challenge.constants.JsonConstants;
import br.com.conexasaude.challenge.constants.RegexConstants;
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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches(message = ApiMessages.PWD_MISMATCH)
public class DoctorDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = ApiMessages.NB_NAME)
    @JsonProperty(value = JsonConstants.VALUE_FIRST_NAME)
    private String firstName;

    @NotBlank(message = ApiMessages.NB_LAST_NAME)
    @JsonProperty(value = JsonConstants.VALUE_LAST_NAME)
    private String lastName;

    @NotBlank(message = ApiMessages.NB_EMAIL)
    @Email(regexp = RegexConstants.REGEX_EMAIL, message = ApiMessages.INV_EMAIL)
    private String email;

    @NotBlank(message = ApiMessages.NB_PWD)
    @JsonProperty(value = JsonConstants.VALUE_PASSWORD, access = JsonProperty.Access.WRITE_ONLY)
    private String pwd;

    @NotBlank(message = ApiMessages.NB_PWD_CONFIRM)
    @JsonProperty(value = JsonConstants.VALUE_PASSWORD_CONFIRMATION, access = JsonProperty.Access.WRITE_ONLY)
    private String pwdConfirmation;

    @NotBlank(message = ApiMessages.NB_SPECIALTY)
    @JsonProperty(value = JsonConstants.VALUE_SPECIALTY)
    private String specialty;

    @NotNull(message = ApiMessages.INV_CPF)
    @CPF(message = ApiMessages.INV_CPF)
    private String cpf;

    @Past(message = ApiMessages.PAST_DATE)
    @JsonProperty(value = JsonConstants.VALUE_DATE_OF_BIRTH)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonConstants.PATTERN_LOCAL_DATE)
    private LocalDate dateOfBirth;

    @Pattern(regexp = RegexConstants.REGEX_TEL, message = ApiMessages.INV_TEL)
    @JsonProperty(value = "telefone")
    private String phoneNumber;
}



