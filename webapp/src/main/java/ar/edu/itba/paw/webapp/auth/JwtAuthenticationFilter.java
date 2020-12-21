package ar.edu.itba.paw.webapp.auth;

import java.io.IOException;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private String jwtAudience;
    private String jwtIssuer;
    private String jwtSecret;
    private String jwtType;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, String jwtAudience, String jwtIssuer,
            String jwtSecret, String jwtType) {
        this.jwtAudience = jwtAudience;
        this.jwtIssuer = jwtIssuer;
        this.jwtSecret = jwtSecret;
        this.jwtType = jwtType;
        this.setAuthenticationManager(authenticationManager);

        setFilterProcessesUrl("/api/login");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain, Authentication authentication) throws IOException {
        User user = (User)authentication.getPrincipal();
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        String token = Jwts.builder()
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .setHeaderParam("typ", jwtType)
                .setIssuer(jwtIssuer)
                .setAudience(jwtAudience)
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                .compact();
            
        final String tokenStr = "Bearer " + token;
        response.addHeader(HttpHeaders.AUTHORIZATION, tokenStr);
        response.setContentType("text/plain");
        response.setContentLength(tokenStr.length());
        response.getWriter().write(tokenStr);
        response.getWriter().flush();
        response.getWriter().close();
    }

}