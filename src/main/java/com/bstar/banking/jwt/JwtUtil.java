package com.bstar.banking.jwt;

import com.bstar.banking.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bstar.banking.common.ExceptionString.CANNOT_CREATE_JWT_TOKEN_WITHOUT_EMAIL;
import static com.bstar.banking.common.ExceptionString.USER_DOES_NOT_HAVE_ANY_PRIVILEGES;
import static com.bstar.banking.common.JwtString.ROLES_CLAIMS;

@RequiredArgsConstructor
@Service
public class JwtUtil {
    private final UserRepository customerRepository;
    private static final int jwtExpirationInMs = 15 * 60 * 60 * 1000;
    private static final int refreshExpirationDateInMs = 50 * 60 * 60 * 1000;
    private String secret;


    @Value("${jwt.secret.key}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        if (StringUtils.isBlank(userDetails.getUsername()))
            throw new IllegalArgumentException(CANNOT_CREATE_JWT_TOKEN_WITHOUT_EMAIL);
        if (userDetails.getAuthorities() == null || userDetails.getAuthorities().isEmpty())
            throw new IllegalArgumentException(USER_DOES_NOT_HAVE_ANY_PRIVILEGES);

        Map<String, Object> claims = new HashMap<>();
        final String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put(ROLES_CLAIMS, authorities);
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        final String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put(ROLES_CLAIMS, authorities);
        return doGenerateRefreshToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    private String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationDateInMs))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Collection<SimpleGrantedAuthority> getRolesFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        Collection<SimpleGrantedAuthority> role = claims.get(ROLES_CLAIMS, Collection.class);
        return role;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getEmailFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
