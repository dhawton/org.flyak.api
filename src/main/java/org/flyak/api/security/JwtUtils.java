package org.flyak.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    private Key key = null;
    @Value("${app.jwt.lifetime}")
    private String jwtLifetime;

    private void checkKeyOrBuild() {
        if (this.key == null) {
            byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        }
    }

    public String generateJwtToken(Authentication authentication) {
        checkKeyOrBuild();
        UserDetailsImpl userPrincipal = (UserDetailsImpl)authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrincipal.getId().toString())
                .claim("name", userPrincipal.getUser().getName())
                .claim("roles", userPrincipal.getAuthorities().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (60000 * Integer.parseInt(jwtLifetime))))
                .setIssuer("org.flyak.api")
                .setAudience("flyak")
                .signWith(this.key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromJwtToken(String token) {
        return buildParser()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public io.jsonwebtoken.JwtParser buildParser() {
        checkKeyOrBuild();

        return Jwts
                .parserBuilder()
                .requireAudience("flyak")
                .requireIssuer("org.flyak.api")
                .setSigningKey(key)
                .build();
    }

    public boolean validateJwtToken(String token) {
        log.debug("Checking token " + token);
        checkKeyOrBuild();

        try {
            buildParser().parse(token);
            return true;
        } catch(SignatureException e) {
            log.error("Caught invalid JWT Signature: " + e.getMessage());
        } catch(MalformedJwtException e) {
            log.error("Caught malformed JWT: " + e.getMessage());
        } catch(ExpiredJwtException e) {
            log.info("Caught expired token.");
        } catch(UnsupportedJwtException e) {
            log.error("Caught unsupported JWT Token: " + e.getMessage());
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error(sw.toString());
        } catch(IllegalArgumentException e) {
            log.error("JWT claims exception or empty, " + e.getMessage());
        }

        return false;
    }
}
