package com.campus.pbotodo.security.utils;

import java.security.KeyPair;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.campus.pbotodo.user.UserToken;
import com.campus.pbotodo.user.UserTokenRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtilities {

    @Autowired
    private KeyPair rsaKeyPair;

    @Autowired
    private UserTokenRepo userTokenRepo;

    public String extractUser(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        HashMap<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails);
    }

    public boolean removeToken(String token) {
        UserToken userToken = userTokenRepo.findByToken(token);
        userToken.setIsActive(false);
        userToken.setUpdatedAt(LocalDateTime.now());
        userTokenRepo.save(userToken);
        return true;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUser(token);
        UserToken userTokenData = userTokenRepo.findByUsernameAndToken(username, token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && userTokenData.getIsActive());
    }

    public String extractBearer(String bearerToken) {
        if (bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }

    private String createToken(HashMap<String, Object> claims, UserDetails subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .signWith(rsaKeyPair.getPrivate(), SignatureAlgorithm.RS256).compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(rsaKeyPair.getPublic()).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

}
