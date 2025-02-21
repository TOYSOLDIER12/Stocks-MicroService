package ma.xproce.stocksmicroservice.Services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;


@Service
public class TokenValidatorImp implements TokenValidator {
    private static final Logger logger = LoggerFactory.getLogger(TokenValidatorImp.class);
    private final JwtDecoder jwtDecoder;


    public TokenValidatorImp(@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkSetUri) {
        if (jwkSetUri == null || jwkSetUri.isEmpty()) {
            throw new IllegalArgumentException("JWK Set URI is not set or is empty");
        }
        this.jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return jwt.getExpiresAt() == null || jwt.getExpiresAt().isAfter(java.time.Instant.now());
        } catch (Exception e) {
            logger.warn("Invalid token: {}", e.getMessage());
            return false;
        }
    }
}
