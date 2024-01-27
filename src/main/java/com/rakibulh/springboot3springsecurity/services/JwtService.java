package com.rakibulh.springboot3springsecurity.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "fhwuefgieuhfghefpioewhfioqhweifriewufhifhiwerfiwdfoighepioehieiodhgoepeori";

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String jwt_token, UserDetails userDetails) {
        final String username = extractUsername(jwt_token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt_token);
    }

    private boolean isTokenExpired(String jwt_token) {
        return extractExpiration(jwt_token).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    public String extractUsername(String jwt_token) {
        return extractClaim(jwt_token, Claims::getSubject);
    }

    public <T> T extractClaim(String jwt_token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(jwt_token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwt_token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwt_token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
