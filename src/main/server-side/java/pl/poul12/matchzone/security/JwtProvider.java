package pl.poul12.matchzone.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;

@Component
//@PropertySource("application.properties")
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${poul12.app.jwtSecret}")
    private String jwtSecret;

    @Value("${poul12.app.jwtExpiration}")
    private int jwtExpiration;

    @Transactional
    public String generateJwtToken(Authentication authentication) {
        logger.info("Iam in JwtProvider1");
        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();
        logger.info("Iam in JwtProvider2");

        return Jwts.builder()
                        .setSubject((userPrincipal.getUsername()))
                        .setIssuedAt(new Date())
                        .setExpiration(new Date((new Date()).getTime() + jwtExpiration*1000))
                        .signWith(SignatureAlgorithm.HS512, jwtSecret)
                        .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            System.out.println("Iam in validateJwtToken");
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e);
        }

        return false;
    }

    public String getUserNameFromJwtToken(String token) {
        logger.info("Iam in getUserNameFromJwtToken");
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}