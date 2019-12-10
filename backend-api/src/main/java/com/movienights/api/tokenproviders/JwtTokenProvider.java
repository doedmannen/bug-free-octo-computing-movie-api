package com.movienights.api.tokenproviders;

import com.movienights.api.configs.MyUserDetailService;
import com.movienights.api.entities.DbUser;
import com.movienights.api.exceptions.CustomException;
import com.movienights.api.repos.DbUserRepo;
import com.movienights.api.watchlists.JwtWatchList;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private String secret = "SOMEKINDOFSHITTYKEYGOESHERE";

    private long validityMillis = 2629743000L;

    private ConcurrentHashMap<String, Long> session = new ConcurrentHashMap<>();

    @Autowired
    MyUserDetailService userDetailService;

    @Autowired
    DbUserRepo dbUserRepo;

    @PostConstruct
    void init(){
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
        JwtWatchList.getInstance();
    }

    public String createToken(String username, String jwtSalt, Set<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority("ROLE_" + s)).collect(Collectors.toList()));
        claims.put("salt", jwtSalt);

        long now = Calendar.getInstance().getTimeInMillis();
        long validity = now + validityMillis;

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(validity))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private boolean validateJwtSalt(String token) {
        DbUser user = dbUserRepo.findDistinctFirstByUsernameIgnoreCase(getUsername(token));
        return user.getJwtSalt().toString().equals(Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("salt"));
    }

    public String getUsername(HttpServletRequest request){
        return getUsername(resolveToken(request));
    }

    public String getUsername(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public DbUser getUserForTokenRenewal(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).setAllowedClockSkewSeconds(604800).parseClaimsJws(token).getBody();
        DbUser user = dbUserRepo.findDistinctFirstByUsernameIgnoreCase(claims.getSubject());
        if(user.getJwtSalt().toString().equals(claims.get("salt"))){
            return user;
        }
        return null;
    }

    public String resolveToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")){
            return token.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            if(JwtWatchList.getInstance().isOnWatchList(getUsername(token)) && !validateJwtSalt(token))
                throw new JwtException("Expired");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
