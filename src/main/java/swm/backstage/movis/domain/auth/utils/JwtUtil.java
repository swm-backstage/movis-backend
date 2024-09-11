package swm.backstage.movis.domain.auth.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

@Getter
@Component
public class JwtUtil {

    @Value("${spring.token.access-token-expired-time}")
    private long ACCESS_TOKEN_EXPIRED_TIME;

    @Value("${spring.token.refresh-token-expired-time}")
    private long REFRESH_TOKEN_EXPIRED_TIME;

    @Value("${spring.token.access-token-name}")
    private String ACCESS_TOKEN_NAME;

    @Value("${spring.token.refresh-token-name}")
    private String REFRESH_TOKEN_NAME;

    private final SecretKey secretKey;

    public JwtUtil(@Value("${spring.token.secret}")String secret) {

        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secret));
    }

    // TODO: Refactoring
    public String getTokenType(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("tokenType", String.class);
    }

    public String getPlatformType(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("platformType", String.class);
    }

    public String getIdentifier(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("identifier", String.class);
    }

    public Date getExpirationDate(String token){

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration();
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String resolveToken(String token) {

        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        return null;
    }

    public void validateToken(String token) throws JwtException {

        Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    }

    public String createToken(String tokenType, String platformType, String identifier, Long expiredTime) {

        return Jwts.builder()
                .claim("tokenType", tokenType)
                .claim("platformType", platformType)
                .claim("identifier", identifier)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredTime))
                .signWith(secretKey)
                .compact();
    }
}
