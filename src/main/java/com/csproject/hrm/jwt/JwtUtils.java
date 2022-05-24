package com.csproject.hrm.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csproject.hrm.services.impl.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class JwtUtils {
    private static final Logger Logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${bezkoder.app.jwtSecret}")
    private String jwtSecret;

    @Value("${bezkoder.app.jwtExpirationMs}")
    private long jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        HashMap<String, Object> map = new HashMap<>();
        map.put("User Data", userPrincipal);
        return Jwts.builder()
                .setSubject(userPrincipal.getId())
                .addClaims(map)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getIdFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(authToken);
        } catch (SignatureException e) {
            Logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            Logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            Logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            Logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            Logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}