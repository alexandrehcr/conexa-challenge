package br.com.conexasaude.challenge.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static br.com.conexasaude.challenge.constants.json.JsonFields.PASSWORD;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    private String email;

    @JsonProperty(value = PASSWORD)
    private String password;
}
