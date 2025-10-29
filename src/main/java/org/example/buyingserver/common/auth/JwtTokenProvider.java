package org.example.buyingserver.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.example.buyingserver.common.dto.ErrorCodeAndMessage;
import org.example.buyingserver.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key SECRET_KEY;
    private final int expiration; // 분 단위

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration}") int expiration
    ) {
        this.expiration = expiration;
        this.SECRET_KEY = new SecretKeySpec(
                Base64.getDecoder().decode(secretKey),
                SignatureAlgorithm.HS512.getJcaName()
        );
    }

    public String createToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration * 60 * 1000L))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    //토큰에서 이메일 추출
    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // email
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeAndMessage.TOKEN_INVALID);
        }
    }
}