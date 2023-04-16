package br.com.conexasaude.challenge.service;

import br.com.conexasaude.challenge.model.Doctor;
import br.com.conexasaude.challenge.model.dto.JwtLog;
import br.com.conexasaude.challenge.repository.DoctorRepository;
import br.com.conexasaude.challenge.repository.JwtLogRepository;
import br.com.conexasaude.challenge.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

// declared bean in the SecurityConfig file
public class JwtLogServiceImpl implements JwtLogService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private JwtLogRepository jwtLogRepository;

    @Autowired
    private JwtUtils jwtUtils;


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

    public void revokeTokenByUserId(Long userId) {
        Optional<JwtLog> savedLog = jwtLogRepository.findCurrentLogByUserId(userId);
        savedLog.ifPresent(log -> {
            log.setRevokedAt(new Date());
            jwtLogRepository.save(log);
        });
    }

    public void revokeToken(String token) {
        long id = jwtUtils.extractUserId(token);
        revokeTokenByUserId(id);
    }

    public void revokeTokenByUsername(String username) {
        long id = doctorRepository.findByEmail(username).get().getId();
        revokeTokenByUserId(id);
    }

    public boolean isTokenValid(String token) {
        long userId = jwtUtils.extractUserId(token);
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
