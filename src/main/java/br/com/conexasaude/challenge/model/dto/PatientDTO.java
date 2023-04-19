package br.com.conexasaude.challenge.model.dto;

import br.com.conexasaude.challenge.constants.ApiMessages;
import br.com.conexasaude.challenge.constants.JsonConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    @NotBlank(message = ApiMessages.NB_NAME)
    @JsonProperty(value = JsonConstants.VALUE_FIRST_NAME)
    private String patientName;

    @NotNull(message = ApiMessages.INV_CPF)
    @CPF(message = ApiMessages.INV_CPF)
    private String cpf;
}
