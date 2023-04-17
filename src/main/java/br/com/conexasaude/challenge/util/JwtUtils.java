package br.com.conexasaude.challenge.util;

import br.com.conexasaude.challenge.constants.SecurityConstants;
import br.com.conexasaude.challenge.model.Doctor;
import br.com.conexasaude.challenge.service.JwtLogService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import static br.com.conexasaude.challenge.constants.SecurityConstants.JWT_EXPIRATION_TIME;
import static br.com.conexasaude.challenge.constants.SecurityConstants.JWT_KEY;


public class JwtUtils {

    private final SecretKey secretKey = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));


    public String createToken(String username, Date now, Date expiration) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();
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

    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }
}
