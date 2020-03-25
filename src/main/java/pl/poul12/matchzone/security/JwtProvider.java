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

        System.out.println("Jestem w JwtProvider.generateJwtToken przed pobraniem principala do UserPrincipal " + new Date());

        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

        System.out.println("Jestem w JwtProvider.generateJwtToken przed zbudowaniem tokena " + new Date());

        String token = Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        System.out.println("Zbudowany token: " + token + "  " + new Date());

        return token;
    }

    public boolean validateJwtToken(String authToken) {
        try {

            System.out.println("Jestem w JwtProvider.validateJwtToken przed walidacją tokena " + new Date());

            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e.getMessage());
        }

        return false;
    }

    public String getUserNameFromJwtToken(String token) {

        System.out.println("Jestem w JwtProvider.getUserNameFromJwtToken przed pobraniem username z tokena " + new Date());

        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}