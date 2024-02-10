package br.com.conexasaude.challenge.integration;

import br.com.conexasaude.challenge.constants.apimessages.ValidationConstants;
import br.com.conexasaude.challenge.model.dto.AuthenticationRequest;
import br.com.conexasaude.challenge.repository.DoctorRepository;
import br.com.conexasaude.challenge.service.JwtLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;
import java.util.TreeMap;

import static br.com.conexasaude.challenge.constants.apimessages.ExceptionMessages.INVALID_USERNAME_PASSWORD;
import static br.com.conexasaude.challenge.constants.json.JsonFields.*;
import static br.com.conexasaude.challenge.util.RemoveMask.removeCpfMask;
import static br.com.conexasaude.challenge.util.RemoveMask.removePhoneMask;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    JwtLogService jwtLogService;

    final String validEmail = "joaocastro_med@email.com";
    final String invalidEmail = "joao@email.com";
    final String validPassword = "12345";
    final String invalidPassword = "1234x";


    private String buildJsonString(Map<String, String> jsonMap) {
        JsonObject jsonObject = new JsonObject();
        jsonMap.forEach((s, s2) -> jsonObject.addProperty(s, s2));
        return jsonObject.toString();
    }

    @Nested
    class RegistrationTest {

        @BeforeEach
        void setup() {
            doctorRepository.deleteAll();
        }

        @DisplayName("Doctor's signup - Positive scenario")
        @Test
        void givenValidRegistrationDTO_whenSignup_thenReturnStatusCreated() throws Exception {
            // Arrange
            String firstName = "firstName";
            String lastName = "lastName";
            String email = "test@email.com";
            String phoneNumber = "(21) 98765-4321";
            String password = "password";
            String specialty = "specialty";
            String cpf = "916.931.920-04";
            String dateOfBirth = "01/01/1990";

            Map<String, String> jsonMap = new TreeMap<>();
            jsonMap.put(FIRST_NAME, firstName);
            jsonMap.put(LAST_NAME, lastName);
            jsonMap.put(EMAIL, email);
            jsonMap.put(PHONE_NUMBER, phoneNumber);
            jsonMap.put(PASSWORD, password);
            jsonMap.put(PASSWORD_CONFIRMATION, password);
            jsonMap.put(SPECIALTY, specialty);
            jsonMap.put("cpf", cpf);
            jsonMap.put(DATE_OF_BIRTH, dateOfBirth);

            String json = buildJsonString(jsonMap);

            assertTrue(doctorRepository.findAll().isEmpty(), "Expected the repository to be empty");

            // Act
            ResultActions response = mockMvc.perform(post("/api/v1/signup")
                    .contentType(MediaType.APPLICATION_JSON).content(json));

            // Assert
            assertFalse(doctorRepository.findAll().isEmpty(), "Expected the repository not to be empty");

            response.andDo(print()).andExpect(status().isCreated())
                    .andExpectAll(
                            jsonPath("$.id", notNullValue()),
                            jsonPath("$." + FIRST_NAME, is(firstName)),
                            jsonPath("$." + LAST_NAME, is(lastName)),
                            jsonPath("$.email", is(email)),
                            jsonPath("$." + SPECIALTY, is(specialty)),
                            jsonPath("$.cpf", is(removeCpfMask(cpf))),
                            jsonPath("$." + DATE_OF_BIRTH, is(dateOfBirth)),
                            jsonPath("$." + PHONE_NUMBER, is(removePhoneMask(phoneNumber)))
                    );
        }

        @DisplayName("Doctor's signup - Negative scenario")
        @Test
        void givenInvalidRegistrationDTO_whenSignup_thenReturnBadRequestWithErrorMessages() throws Exception {
            // Arrange
            Map<String, String> jsonMap = new TreeMap<>();
            jsonMap.put(FIRST_NAME, "");
            jsonMap.put(LAST_NAME, "");
            jsonMap.put(EMAIL, "");
            jsonMap.put(PHONE_NUMBER, "");
            jsonMap.put(PASSWORD, "12345");
            jsonMap.put(PASSWORD_CONFIRMATION, "1234x");
            jsonMap.put(SPECIALTY, "");
            jsonMap.put("cpf", "123.123.123-01");
            jsonMap.put(DATE_OF_BIRTH, "01/01/2030");

            String json = buildJsonString(jsonMap);

            assertTrue(doctorRepository.findAll().isEmpty(), "Expected the repository to be empty");


            // Act
            ResultActions response = mockMvc.perform(post("/api/v1/signup")
                    .contentType(MediaType.APPLICATION_JSON).content(json));

            // Assert
            assertTrue(doctorRepository.findAll().isEmpty(), "Expected the repository to be empty");

            response.andDo(print()).andExpect(status().isBadRequest())
                    .andExpectAll(
                            jsonPath("$.messages",
                                    containsInAnyOrder(
                                            ValidationConstants.NB_NAME,
                                            ValidationConstants.NB_LAST_NAME,
                                            ValidationConstants.NB_EMAIL,
                                            ValidationConstants.INV_EMAIL,
                                            ValidationConstants.PWD_MISMATCH,
                                            ValidationConstants.NB_SPECIALTY,
                                            ValidationConstants.INV_CPF,
                                            ValidationConstants.INV_TEL,
                                            ValidationConstants.PAST_DATE
                                    )));
        }
    }

    @DisplayName("Login attempt - Positive scenario")
    @Test
    void givenRightCredentials_whenLoggingIn_thenReturnOkAndJWT() throws Exception {
        // Arrange
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(validEmail, validPassword);

        // Act
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(authenticationRequest)));

        // Assert
        result.andDo(print())
                .andExpectAll(
                        status().isOk(),
                        header().string(AUTHORIZATION, startsWith("Bearer ey")),
                        jsonPath("$", startsWith("ey"))
                );
    }

    @DisplayName("Login attempt - Invalid password")
    @Test
    void givenInvalidPrincipal_whenLoggingIn_thenReturnUnauthorized() throws Exception {
        // Arrange
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(validEmail, invalidPassword);

        // Act
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(authenticationRequest)));

        // Assert
        result.andDo(print())
                .andExpectAll(
                        status().isUnauthorized(),
                        header().string(AUTHORIZATION, nullValue()),
                        jsonPath("$.message", containsString(INVALID_USERNAME_PASSWORD))
                );
    }

    @DisplayName("Login attempt - Invalid username")
    @Test
    void givenInvalidCredentials_whenLoggingIn_thenReturnUnauthorized() throws Exception {
        // Arrange
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(invalidEmail, validPassword);

        // Act
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(authenticationRequest)));

        // Assert
        result.andDo(print())
                .andExpectAll(
                        status().isUnauthorized(),
                        header().string(AUTHORIZATION, nullValue()),
                        jsonPath("$.message", containsString(INVALID_USERNAME_PASSWORD))
                );
    }

    @DisplayName("Logoff")
    @Test
    void givenValidJwt_whenLogoff_thenInvalidateJwt() throws Exception {
        // Arrange
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(validEmail, validPassword);

        ResultActions login = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(authenticationRequest)));

        String token = login.andReturn().getResponse().getContentAsString();

        assertTrue(jwtLogService.isTokenValid(token), "Expected token to be valid, but it's not.");

        // Act
        ResultActions resultActions = mockMvc.perform(get("/api/v1/logoff").header(AUTHORIZATION, "Bearer " + token));

        // Assert
        resultActions.andDo(print()).andExpect(status().isOk());
        assertFalse(jwtLogService.isTokenValid(token), "Expected token to be revoked, but it's still valid.");
    }
}
