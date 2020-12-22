package com.example.demo.utils.sec.jwt;

import com.example.demo.services.UserServiceDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtil {

    private final String KEY = "deadinside";
    private static final long TOKEN_VALIDITY = 1800000; //30 min
    private final UserServiceDetails userDetails;


    @Autowired
    public JWTUtil(UserServiceDetails userDetails) {
        this.userDetails = userDetails;

    }

    public String generateToken(String username, List<String> roles) {
        System.out.println("Генерируем токен " + LocalDateTime.now());
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(new Date(now.getTime() + TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, KEY).compact();
    }

    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token);
        if (claims.getBody().getExpiration().before(new Date())) {
            return false;
        } else return true;
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDate(token);
        return expiration.before(new Date());
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails ud = this.userDetails.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(ud, "", ud.getAuthorities());
    }

    public String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public Date getExpirationDate(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }
}