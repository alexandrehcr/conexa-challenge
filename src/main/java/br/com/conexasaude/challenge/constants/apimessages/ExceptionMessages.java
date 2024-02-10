package br.com.conexasaude.challenge.constants.apimessages;

public record ExceptionMessages() {
     public static final String INVALID_USERNAME_PASSWORD = "Usuário ou senha inválidos.";
     public static final String UNSUPPORTED_JWT_EXCEPTION = "Token não suportado.";
     public static final String MALFORMED_JWT_EXCEPTION =   "Token inválido.";
     public static final String INVALID_JWT_EXCEPTION =     "Token inválido.";
     public static final String SIGNATURE_EXCEPTION =       "Assinatura do token inválida.";
     public static final String ILLEGAL_ARGUMENT_EXCEPTION = "Payload do token inválido.";
     public static final String HTTP_METHOD_NOT_ALLOWED =   "Esperava %s, mas foi recebido %s."; // expected method, received method
     public static final String PATIENT_NOT_FOUND =         "Não há registro de paciente com o CPF '%s'.";
}
