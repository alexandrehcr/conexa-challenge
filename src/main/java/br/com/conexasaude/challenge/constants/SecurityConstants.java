package br.com.conexasaude.challenge.constants;

public record SecurityConstants() {
    public static final String HOME_PATH = "/";
    public static final String LOGIN_PATH = "/api/v1/login";
    public static final String REGISTRATION_PATH = "/api/v1/signup";
    public static final String LOGOUT_PATH = "/api/v1/logoff";
    public static final String BEARER = "Bearer ";
    public static final String JWT_KEY = "7x!A%D*G-KaPdSgVkYp3s6v8y/B?E(H+MbQeThWmZq4t7w!z$C&F)J@NcRfUjXn2";
    public static final Long   JWT_EXPIRATION_TIME = 36_000_000L; // 10 hours
}
