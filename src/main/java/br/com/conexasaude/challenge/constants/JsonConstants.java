package br.com.conexasaude.challenge.constants;

// Allows to quickly change JSON's property values and patterns in a single place.
public interface JsonConstants {

    // values
    String VALUE_FIRST_NAME = "nome";
    String VALUE_LAST_NAME = "sobrenome";
    String VALUE_EMAIL = "email";
    String VALUE_PASSWORD = "senha";
    String VALUE_PASSWORD_CONFIRMATION = "confirmacaoSenha";
    String VALUE_SPECIALTY = "especialidade";
    String VALUE_DATE_OF_BIRTH = "dataNascimento";
    String VALUE_LOCAL_DATE_TIME = "dataHora";
    String VALUE_PHONE_NUMBER = "telefone";
    String VALUE_PATIENT = "paciente";
    String VALUE_DOCTOR = "medico";
    String VALUE_PATIENT_NAME = "nomePaciente";

    // patterns
    String PATTERN_LOCAL_DATE = "dd/MM/yyyy";
    String PATTERN_LOCAL_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
}
