package br.com.conexasaude.challenge.model.dto;

import br.com.conexasaude.challenge.constants.JsonConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    private String email;

    @JsonProperty(value = JsonConstants.VALUE_PASSWORD)
    private String password;
}
