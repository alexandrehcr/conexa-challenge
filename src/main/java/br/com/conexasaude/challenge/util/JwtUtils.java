package br.com.conexasaude.challenge.util;

import br.com.conexasaude.challenge.constants.SecurityConstants;
import br.com.conexasaude.challenge.model.Doctor;
import br.com.conexasaude.challenge.model.dto.JwtLog;
import br.com.conexasaude.challenge.repository.DoctorRepository;
import br.com.conexasaude.challenge.repository.JwtLogRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static br.com.conexasaude.challenge.constants.SecurityConstants.JWT_EXPIRATION_TIME;
import static br.com.conexasaude.challenge.constants.SecurityConstants.JWT_KEY;

@Component
public class JwtUtils {

    final SecretKey secretKey = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private JwtLogRepository jwtLogRepository;

    public String createToken(Authentication authentication) {
        Date now = new Date();
        Date expiration = new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME);
        Doctor user = doctorRepository.findByEmail(authentication.getName()).get();
        Long userId = user.getId();

        String jwt =  Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .addClaims(Map.of("userId", userId))
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();

        saveJwtLog(now, expiration, user, jwt);
        return jwt;
    }

    private JwtLog saveJwtLog(Date now, Date expiration, Doctor user, String token) {
        JwtLog jwtLog = JwtLog.builder()
                .jwt(token)
                .issuedAt(now)
                .expiration(expiration)
                .doctor(user)
                .build();

        // Expires a previous token the user may have
        revokeTokenByUserId(user.getId());
        return jwtLogRepository.save(jwtLog);
    }

    public void revokeToken(String token) {
        Long userId = extractUserId(token);
        revokeTokenByUserId(userId);
    }

    public void revokeTokenByUserId(Long userId) {
        Optional<JwtLog> savedLog = jwtLogRepository.findCurrentLogByUserId(userId);
        savedLog.ifPresent(log -> {
            log.setRevokedAt(new Date());
            jwtLogRepository.save(log);
        });
    }

    public boolean isTokenInvalidOrRevoked(String token) {
        Long userId = extractUserId(token);

        Optional<JwtLog> log = jwtLogRepository.findCurrentLogByUserId(userId);
        if (log.isEmpty()) {
            return true;
        } else {
           return !log.get().getJwt().equals(token);
        }
    }

    public String extractTokenFromRequest(ServletRequest request) {
        String header = ((HttpServletRequest) request).getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(SecurityConstants.BEARER)) {
            return header.substring(7);
        }
        return null;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }
}
