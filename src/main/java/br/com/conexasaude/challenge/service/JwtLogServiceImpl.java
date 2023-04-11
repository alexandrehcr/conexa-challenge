package br.com.conexasaude.challenge.service;

import br.com.conexasaude.challenge.model.Doctor;
import br.com.conexasaude.challenge.model.dto.JwtLog;
import br.com.conexasaude.challenge.repository.DoctorRepository;
import br.com.conexasaude.challenge.repository.JwtLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtLogServiceImpl implements JwtLogService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private JwtLogRepository jwtLogRepository;

    public JwtLog saveJwtLog(Date now, Date expiration, Long userId, String token) {
        Doctor user = doctorRepository.findById(userId).get();

        JwtLog jwtLog = JwtLog.builder()
                .jwt(token)
                .issuedAt(now)
                .expiration(expiration)
                .user(user)
                .build();

        // Expires a previous token the user may have
        revokeTokenByUserId(userId);
        return jwtLogRepository.save(jwtLog);
    }

    public void revokeTokenByUserId(Long userId) {
        Optional<JwtLog> savedLog = jwtLogRepository.findCurrentLogByUserId(userId);
        savedLog.ifPresent(log -> {
            log.setRevokedAt(new Date());
            jwtLogRepository.save(log);
        });
    }

    public boolean isTokenValid(String token, Long userId) {
        Optional<JwtLog> log = jwtLogRepository.findCurrentLogByUserId(userId);
        if (log.isEmpty()) {
            return false;
        } else {
            return log.get().getJwt().equals(token);
        }
    }

    public <T> T getFromUser(Authentication authentication, Function<Doctor, T> function) {
        Doctor user = doctorRepository.findByEmail(authentication.getName()).get();
        return function.apply(user);
    }
}
