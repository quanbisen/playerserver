package com.util;

import com.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * @author super lollipop
 * @date 20-2-26
 */
@Component
public class JwtUtils {

    @Autowired
    private JwtConfig jwtConfig;

    private static JwtConfig JWT_CONFIG;

    @PostConstruct
    public void init(){
        JWT_CONFIG = jwtConfig;
    }

    public static String generateToken(Map<String,Object> map, Date generateTime){
        String jwt = Jwts.builder().
                signWith(Keys.hmacShaKeyFor(JWT_CONFIG.getKey().getBytes())).
                addClaims(map).
                setIssuer(JWT_CONFIG.getIssuer()).
                setExpiration(new Date(generateTime.getTime()+ JWT_CONFIG.getExpiration())).
                compact();
        return jwt;
    }

    public Boolean validateToken(String token, String id) {
        String subject = extractID(token);
        return subject.equals(id) && !isTokenExpired(token);
    }

    public static String extractID(String token) throws SignatureException {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("id");
    }

    public static String extractPassword(String token){
        Claims claims = extractAllClaims(token);
        return (String) claims.get("password");
    }

    public static Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public static Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(JWT_CONFIG.getKey().getBytes())).parseClaimsJws(token).getBody();
    }
}
