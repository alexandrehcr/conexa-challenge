package br.com.conexasaude.challenge.constants.apimessages;

public record UniqueConstraintViolationMessages() {
     public static final String ATTENDANCE_UNIQUE_CONSTRAINT_VIOLATION = "Não foi possível realizar o agendamento. Você ou o paciente já têm uma consulta agendada para este horário.";
     public static final String EMAIL_UNIQUE_CONSTRAINT_VIOLATION =      "Este email já está cadastrado.";
     public static final String CPF_UNIQUE_CONSTRAINT_VIOLATION =        "Este CPF já está cadastrado.";
     public static final String EMAIL_AND_CPF_UNIQUE_CONSTRAINT_VIOLATION = "Email e CPF já cadastrados.";
}
