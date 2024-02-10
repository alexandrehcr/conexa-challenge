package br.com.conexasaude.challenge.service;

import br.com.conexasaude.challenge.model.Doctor;
import br.com.conexasaude.challenge.model.dto.JwtLog;
import br.com.conexasaude.challenge.repository.DoctorRepository;
import br.com.conexasaude.challenge.repository.JwtLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class JwtLogService {

    private DoctorRepository doctorRepository;
    private JwtLogRepository jwtLogRepository;
    private JwtService jwtService;

    public JwtLog saveJwtLog(String username, String token, Date now, Date expiration) {
        Doctor user = doctorRepository.findByEmail(username).get();

        JwtLog jwtLog = JwtLog.builder()
                .jwt(token)
                .issuedAt(now)
                .expiration(expiration)
                .user(user)
                .build();

        return jwtLogRepository.save(jwtLog);
    }

    public boolean isTokenValid(String token) {
        String username = jwtService.extractUsername(token);
        Optional<Doctor> user = doctorRepository.findByEmail(username);

        Optional<JwtLog> log = jwtLogRepository.findCurrentLogByUserId(user.get().getId());
        if (log.isEmpty()) {
            return false;
        } else {
            return log.get().getJwt().equals(token);
        }
    }

    public void revokeToken(String token) {
        String username = jwtService.extractUsername(token);
        revokeTokenByUsername(username);
    }

    public void revokeTokenByUsername(String username) {
        long id = doctorRepository.findByEmail(username).get().getId();
        revokeTokenByUserId(id);
    }

    public void revokeTokenByUserId(Long userId) {
        Optional<JwtLog> savedLog = jwtLogRepository.findCurrentLogByUserId(userId);
        savedLog.ifPresent(log -> {
            log.setRevokedAt(new Date());
            jwtLogRepository.save(log);
        });
    }

    public <T> T getFromUser(Authentication authentication, Function<Doctor, T> function) {
        Doctor user = doctorRepository.findByEmail(authentication.getName()).get();
        return function.apply(user);
    }
}
