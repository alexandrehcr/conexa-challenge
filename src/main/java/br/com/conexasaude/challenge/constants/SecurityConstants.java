package br.com.conexasaude.challenge.constants;

public interface SecurityConstants {

    String HOME_PATH = "/";
    String LOGIN_PATH = "/api/v1/login";
    String REGISTRATION_PATH = "/api/v1/signup";
    String LOGOUT_PATH = "/api/v1/logoff";
    String BEARER = "Bearer ";
    String JWT_KEY = "7x!A%D*G-KaPdSgVkYp3s6v8y/B?E(H+MbQeThWmZq4t7w!z$C&F)J@NcRfUjXn2";
    long JWT_EXPIRATION_TIME = 36_000_000; // 10 hours
}
