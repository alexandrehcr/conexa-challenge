package br.com.conexasaude.challenge.constants;

public record RegexConstants() {
    // The following REGEX is meant to match Brazilian phone numbers.
    public static final String REGEX_TEL = "^\\(?(\\d{2}\\)? ?)\\d{4,5}[ -]?\\d{4}$";
    public static final String REGEX_EMAIL = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\+[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
}
