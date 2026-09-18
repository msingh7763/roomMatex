package com.RoomMateX.security;


import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*; //JWT library

import java.security.Key;
import java.security.Signature;
import java.util.Date;

@Component //register this class as a Spring bean
public class JwtUtil {  //spring manages this class

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // jet signing key used to sign token and verify token if someone changes token without this -> verification fails. password for token
    private final long EXPIRATION = 1000 * 60 * 60 * 24; //1 day



    public String generateToken(String email){  //called when user logs in. We are passing user email

        return Jwts.builder()     //creates JWT builder
                .setSubject(email)   //Stores email inside token
                .setIssuedAt(new Date())  //when token created
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))  //adds expiry timestamp
                .signWith(key)  // signs token using: Algorithm: HS256 Key: SECRET Creates cryptographic signature.
                .compact();   //Converts everything into final JWT string: xxxxx.yyyyy.zzzzz (Header.Payload.Signature)
    }
    // Reads token. Gets claims. Returns Subject (email).
    public String extractEmail(String token){

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //Checks: is expiration AFTER now? is yes -> valid. if expired -> false.
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }



}
