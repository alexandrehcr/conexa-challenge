package br.com.conexasaude.challenge.service;

import br.com.conexasaude.challenge.model.Doctor;
import br.com.conexasaude.challenge.model.dto.JwtLog;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public interface JwtLogService {
  JwtLog saveJwtLog(String username, String token, Date now, Date expiration);
  void revokeToken(String token);
  void revokeTokenByUserId(Long userId);
  void revokeTokenByUsername(String username);
  boolean isTokenValid(String token);
  <T> T getFromUser(Authentication authentication, Function<Doctor, T> function);
}