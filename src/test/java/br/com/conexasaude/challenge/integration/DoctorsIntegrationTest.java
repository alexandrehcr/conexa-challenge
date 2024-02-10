package br.com.conexasaude.challenge.integration;

import br.com.conexasaude.challenge.model.dto.AuthenticationRequest;
import br.com.conexasaude.challenge.model.dto.PatientDTO;
import br.com.conexasaude.challenge.repository.AttendanceRepository;
import br.com.conexasaude.challenge.service.JwtLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static br.com.conexasaude.challenge.constants.apimessages.UniqueConstraintViolationMessages.ATTENDANCE_UNIQUE_CONSTRAINT_VIOLATION;
import static br.com.conexasaude.challenge.constants.json.JsonFields.PATIENT;
import static br.com.conexasaude.challenge.constants.json.JsonPatterns.LOCAL_DATE_TIME_PAT;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//* For some reason this class' tests only passes when it runs separately
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DoctorsIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtLogService jwtLogService;

    @Autowired
    AttendanceRepository attendanceRepository;

    final String dateAndTimeString = "2024-01-01 09:00:00";


    @DisplayName("View own scheduled attendances - positive scenario")
    @Test
    void givenValidJwt_whenUserVerifyAttendances_thenReturnUserAttendances() throws Exception {
        String token;
        int userAttendances;

        // Test 1
        token = getToken("joaocastro_med@email.com", "12345");
        userAttendances = 4;
        testAuthorizedUserViewAttendances(token, userAttendances);

        // Test 2
        token = getToken("fervasconcelos_med@email.com", "12345");
        userAttendances = 2;
        testAuthorizedUserViewAttendances(token, userAttendances);

        // Test 3
        token = getToken("antoniomoura_med@email.com", "12345");
        userAttendances = 0;
        testAuthorizedUserViewAttendances(token, userAttendances);
    }

    private void testAuthorizedUserViewAttendances(String token, int numberOfAttendances) throws Exception {
        assertNotNull(token, "Expected token not to be null");

        // Act
        ResultActions result = mockMvc.perform(get("/api/v1/view-attendances").header(AUTHORIZATION, "Bearer " + token));
        List<Object> responseBody = JsonPath.parse(result.andReturn().getResponse().getContentAsString()).read("$");

        // Assert
        result.andDo(print()).andExpectAll(status().isOk(), jsonPath("$", notNullValue()));

        assertEquals(numberOfAttendances, responseBody.size(),
                String.format("Expected user to have %d attendances, but found %d.", numberOfAttendances, responseBody.size()));

        assertTrue(attendanceRepository.findAll().size() > responseBody.size(),
                "Expected AttendanceRepository to have more records than what was returned by the response body.");
    }


    @DisplayName("View own scheduled attendances - Invalid token")
    @Test
    void givenInvalidJwt_whenUserVerifyAttendances_thenReturnUnauthorized() throws Exception {
        // Arrange
        // https://jwt.io/ default
        final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        // Act
        ResultActions result = mockMvc.perform(get("/api/v1/view-attendances").header(AUTHORIZATION, "Bearer " + token));

        // Assert
        result.andDo(print()).andExpect(status().isUnauthorized());
    }


    @DisplayName("Schedule attendance - Positive scenario")
    @Test
    void givenValidAttendanceDTO_whenSaveAttendance_thenRegisterSave() throws Exception {
        // Arrange
        final String token = getToken("gabrielacampos_med@email.com", "12345");

        // Patient's CPF must be registered in the database in order for the attendance to be successfully registered.
        String attendanceJsonString = getAttendanceScheduleJsonString("Raul Matos", "746.555.467-51", dateAndTimeString);

        // Act
        ResultActions result = postAttendance(token, attendanceJsonString);

        // Assert
        result.andDo(print()).andExpect(status().isCreated());
    }

    @DisplayName("Schedule attendance with non-existing patient")
    @Test
    void givenInvalidAttendanceDTO_whenSaveAttendance_thenReturnBadRequest() throws Exception {
        // Arrange
        final String patientCpf = "123.123.123-01";
        final String token = getToken("gabrielacampos_med@email.com", "12345");
        String invalidPatientJson = getAttendanceScheduleJsonString("Vera Cruz", patientCpf, dateAndTimeString);

        // Act
        ResultActions result = postAttendance(token, invalidPatientJson);

        // Assert
        result.andDo(print()).andExpect(status().isNotFound());
    }

    @Nested
    class ConflictingAttendancesTest {

        @BeforeEach
        void setup() throws Exception {
            String token = getToken("gabrielacampos_med@email.com", "12345");
            // Schedule attendance to conflict
            String attendanceJsonString = getAttendanceScheduleJsonString("Raul Matos", "746.555.467-51", dateAndTimeString);

            mockMvc.perform(post("/api/v1/attendance").header(AUTHORIZATION, "Bearer " + token)
                    .contentType(APPLICATION_JSON).content(attendanceJsonString));
        }

        // user 'gabrielacampos_med@email.com' already has an attendance scheduled for this date and time, set on setup()
        @DisplayName("User's conflicting schedule attendance")
        @Test
        void givenUserConflictingDateTimeInAttendanceDTO_whenSaveAttendance_thenReturnBadRequest() throws Exception {
            String token = getToken("gabrielacampos_med@email.com", "12345");
            String attendanceJsonString = getAttendanceScheduleJsonString("Carolina Peixoto", "766.185.806-17", dateAndTimeString);
            ResultActions result = postAttendance(token, attendanceJsonString);

            // Assert
            result.andDo(print()).andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.messages[0]", is(ATTENDANCE_UNIQUE_CONSTRAINT_VIOLATION))
            );
        }

        // patient with cpf '746.555.467-51' already has an attendance scheduled for this date and time, set on setup
        @DisplayName("Patient's conflicting schedule attendance")
        @Test
        void givenPatientConflictingDateTimeInAttendanceDTO_whenSaveAttendance_thenReturnBadRequest() throws Exception {
            final String token = getToken("joaocastro_med@email.com", "12345");
            String attendanceJson = getAttendanceScheduleJsonString("Raul Matos", "746.555.467-51", dateAndTimeString);
            ResultActions result = postAttendance(token, attendanceJson);

            // Assert
            result.andDo(print()).andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.messages[0]", containsString(ATTENDANCE_UNIQUE_CONSTRAINT_VIOLATION))
            );
        }
    }

    private ResultActions postAttendance(String token, String attendanceJsonString) throws Exception {
        return mockMvc.perform(post("/api/v1/attendance").header(AUTHORIZATION, "Bearer " + token)
                .contentType(APPLICATION_JSON).content(attendanceJsonString));
    }

    private String getToken(String username, String password) throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(username, password);

        ResultActions login = mockMvc.perform(post("/api/v1/login")
                .contentType(APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(authenticationRequest)));

        String token = login.andReturn().getResponse().getContentAsString();
        assertTrue(jwtLogService.isTokenValid(token), "Expected token to be valid, but it's not.");

        return token;
    }

    private String getAttendanceScheduleJsonString(String patientName, String patientCpf, String formattedLocalDateTime) {
        JsonElement patientJson = new Gson().toJsonTree(new PatientDTO(patientName, patientCpf));
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(LOCAL_DATE_TIME_PAT, formattedLocalDateTime);
        jsonObject.add(PATIENT, patientJson);
        return jsonObject.toString();
    }
}
