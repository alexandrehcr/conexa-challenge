package br.com.conexasaude.challenge.constants;

// Allows to quickly change the API messages in a single place.
public interface ApiMessages {

    // Exception messages
    String INVALID_USERNAME_PASSWORD = "Usuário ou senha inválidos.";
    String UNSUPPORTED_JWT_EXCEPTION = "Token não suportado.";
    String MALFORMED_JWT_EXCEPTION = "Token inválido.";
    String SIGNATURE_EXCEPTION = "Assinatura do token inválida.";
    String ILLEGAL_ARGUMENT_EXCEPTION = "Payload do token inválido.";
    String METHOD_NOT_ALLOWED = "Esperava %s, mas foi recebido %s."; // expected method / received method

    // Unique constraint violations -- DataIntegrityViolationException.class
    String ATTENDANCE_UNIQUE_CONSTRAINT_VIOLATION = "Não foi possível realizar o agendamento. Você ou o paciente já têm uma consulta agendada para este horário.";
    String EMAIL_UNIQUE_CONSTRAINT_VIOLATION = "Este email já está cadastrado.";
    String CPF_UNIQUE_CONSTRAINT_VIOLATION = "Este CPF já está cadastrado.";
    String EMAIL_AND_CPF_UNIQUE_CONSTRAINT_VIOLATION = "Email e CPF já cadastrados.";

    // EntityNotFoundException
    String PATIENT_NOT_FOUND = "Não há registro de paciente com o CPF '%s'.";

    // Validation messages
    String PWD_MISMATCH = "As senhas não conferem.";
    String FUTURE_DATE = "A data deve ser futura.";
    String PAST_DATE = "A data deve estar no passado.";
    String INV_CPF = "CPF inválido.";
    String INV_EMAIL = "Email inválido";
    String INV_TEL = "Número de telefone inválido.";

    String CAMPO = "O campo ";
    String NOT_BLANK = " não pode ser vazio.";
    String NB_NAME = CAMPO + JsonConstants.VALUE_FIRST_NAME + NOT_BLANK;
    String NB_LAST_NAME = CAMPO + JsonConstants.VALUE_LAST_NAME + NOT_BLANK;
    String NB_SPECIALTY = CAMPO + JsonConstants.VALUE_SPECIALTY + NOT_BLANK;
    String NB_EMAIL = CAMPO + JsonConstants.VALUE_EMAIL + NOT_BLANK;
    String NB_PWD = CAMPO + JsonConstants.VALUE_PASSWORD + NOT_BLANK;
    String NB_PWD_CONFIRM = CAMPO + JsonConstants.VALUE_PASSWORD_CONFIRMATION + NOT_BLANK;
    String NB_PATIENT = CAMPO + JsonConstants.VALUE_PATIENT + NOT_BLANK;
}
