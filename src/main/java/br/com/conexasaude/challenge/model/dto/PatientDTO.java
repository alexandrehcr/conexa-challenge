package br.com.conexasaude.challenge.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import static br.com.conexasaude.challenge.constants.apimessages.ValidationConstants.INV_CPF;
import static br.com.conexasaude.challenge.constants.apimessages.ValidationConstants.NB_NAME;
import static br.com.conexasaude.challenge.constants.json.JsonFields.FIRST_NAME;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    @NotBlank(message = NB_NAME)
    @JsonProperty(value = FIRST_NAME)
    private String patientName;

    @NotNull(message = INV_CPF)
    @CPF(message = INV_CPF)
    private String cpf;
}
