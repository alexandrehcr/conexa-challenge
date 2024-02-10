package br.com.conexasaude.challenge.constants.apimessages;

import br.com.conexasaude.challenge.constants.json.JsonFields;

public record ValidationConstants () {
    public static final String PWD_MISMATCH = "As senhas não conferem.";
    public static final String FUTURE_DATE =  "A data deve ser futura.";
    public static final String PAST_DATE =    "A data deve estar no passado.";
    public static final String INV_CPF =      "CPF inválido.";
    public static final String INV_EMAIL =    "Email inválido";
    public static final String INV_TEL =      "Número de telefone inválido.";

    public static final String NB_NAME =      "O campo " + JsonFields.FIRST_NAME  + " não pode ser vazio.";
    public static final String NB_LAST_NAME = "O campo " + JsonFields.LAST_NAME   + " não pode ser vazio.";
    public static final String NB_SPECIALTY = "O campo " + JsonFields.SPECIALTY   + " não pode ser vazio.";
    public static final String NB_EMAIL =     "O campo " + JsonFields.EMAIL       + " não pode ser vazio.";
    public static final String NB_PWD =       "O campo " + JsonFields.PASSWORD    + " não pode ser vazio.";
    public static final String NB_PATIENT =   "O campo " + JsonFields.PATIENT     + " não pode ser vazio.";
}
