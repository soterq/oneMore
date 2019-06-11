package com.unifun.voice.jwt;

import io.jsonwebtoken.*;
import lombok.NoArgsConstructor;

import javax.xml.bind.DatatypeConverter;
import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
public class TokenManager {

    public  boolean verifyIfTokenIsValid(String jwt, String secretKey) {
        Claims claims = decodeJWT(jwt, secretKey);
        if(claims == null) {
            return false;
        }

        return !verifyIfTokenIsExpired(claims);
    }

    private Claims decodeJWT(String jwt, String secretKey) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .parseClaimsJws(jwt).getBody();
            return claims;
        } catch(io.jsonwebtoken.security.SignatureException e) {
            System.out.println("Invalid Token");
            return null;
        }

    }

    private  boolean verifyIfTokenIsExpired(Claims claim) {
        Date expiration = claim.getExpiration();
        // verify if expired and remove the specific refresh token
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Date currentDate = new Date(ts.getTime());

        if(currentDate.after(expiration)) {
            System.out.println("Expired Token");
            return true;
        }
        return false;
    }
}