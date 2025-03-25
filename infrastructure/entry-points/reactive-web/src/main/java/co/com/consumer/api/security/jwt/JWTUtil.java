package co.com.consumer.api.security.jwt;

import co.com.consumer.model.exceptions.JWTException;
import co.com.consumer.model.userapp.UserApp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static co.com.consumer.model.utils.AppConstants.ROLES;

@Slf4j
@Component
public class JWTUtil {

    private final String secret;
    private final String expirationRefresh;
    private final String expirationAccessToken;
    private SecretKey secretKey;

    public JWTUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") String expirationRefresh,@Value("${jwt.expirationAccess}") String expirationAccessToken) {
        this.secret = secret;
        this.expirationRefresh = expirationRefresh;
        this.expirationAccessToken = expirationAccessToken;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims getAllClaimsFromToken(String token) {
        try {
            log.info("Validating token");
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        } catch (SecurityException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException |
                 IllegalArgumentException e) {
            throw new JWTException(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateRefreshToken(UserApp user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLES, user.getRoles());
        int timeInHours = Integer.parseInt(expirationRefresh);
        return createToken(claims, user.getUsername(), timeInHours);
    }

    public String generateAccessToken(UserApp user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ROLES, user.getRoles());
        int timeInMinutes = Integer.parseInt(expirationAccessToken);
        return createToken(claims, user.getUsername(), timeInMinutes);
    }

    private String createToken(Map<String, Object> claims, String subject, int timeLongDays) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + timeLongDays * 1000L);
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(createdDate)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        final String extractUsername = extractUsername(token);
        return (extractUsername.equals(username) && !isTokenExpired(token));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
}
