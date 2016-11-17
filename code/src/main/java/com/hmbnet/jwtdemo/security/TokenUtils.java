package com.hmbnet.jwtdemo.security;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.hmbnet.jwtdemo.model.JwtUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenUtils {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expiration}")
    private Long expiration;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            created = claims.getIssuedAt();
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(this.secret.getBytes("UTF-8")).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + this.expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = this.getExpirationDateFromToken(token);
        if (expiration != null) {
            return expiration.before(new Date());
        }
        return true;
    }

    public String generateToken(String username, String roles) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("sub", username);
        claims.put("roles", roles);
        return this.generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        try {
            return Jwts.builder().setHeaderParam("typ", "JWT").setClaims(claims).setIssuer("hmbnet.com")
                    .setExpiration(this.generateExpirationDate()).setIssuedAt(new Date())
                    .signWith(SignatureAlgorithm.HS256, this.secret.getBytes("UTF-8")).compact();
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean canTokenBeRefreshed(String token) {
        return !this.isTokenExpired(token);
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            refreshedToken = this.generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token) {
        return !this.isTokenExpired(token);
    }

    public UserDetails buildUserDetailsFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("roles"));
        JwtUserDetails jwtDetails = new JwtUserDetails(claims.getSubject(), null, validateToken(token), auths);

        return jwtDetails;
    }

}
