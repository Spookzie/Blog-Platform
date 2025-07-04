package com.spookzie.Blog_Platform.services.impl;

import com.spookzie.Blog_Platform.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService
{
    /*  Instance variables  */
    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    private final Long jwtExpiryMs = 86400000L;


    /*  Implemented Methods */
    @Override
    public UserDetails authenticate(String email, String password)
    {
        this.authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        return this.userDetailsService.loadUserByUsername(email);
    }


    @Override
    public String generateToken(UserDetails user_details)
    {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user_details.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiryMs))
                .signWith(this.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    @Override
    public UserDetails validateToken(String token)
    {
        String username = this.extractUsername(token);

        return this.userDetailsService.loadUserByUsername(username);
    }


    private Key getSigningKey()
    {
        byte[] keyBytes = this.secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);    // Building a key for HMAC algorithms
    }

    /*  Decoding & validating the JWT and then extracting the username from it  */
    private String extractUsername(String token)
    {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}