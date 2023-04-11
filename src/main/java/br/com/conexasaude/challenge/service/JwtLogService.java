package br.com.conexasaude.challenge.service;

import br.com.conexasaude.challenge.model.Doctor;
import br.com.conexasaude.challenge.model.dto.JwtLog;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public interface JwtLogService {
  JwtLog saveJwtLog(Date now, Date expiration, Long userId, String token);
  void revokeTokenByUserId(Long userId);
  boolean isTokenValid(String token, Long userId);
  <T> T getFromUser(Authentication authentication, Function<Doctor, T> function);
}