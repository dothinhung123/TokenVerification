package com.go.tokenverification.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.go.tokenverification.jwt.JwtToken;
import com.go.tokenverification.jwt.JwtTokenService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationProvider authenticationManager;

    private final JwtTokenService jwtTokenService;

    private ObjectMapper objectMapper = new ObjectMapper();
    public AuthenticationFilter(AuthenticationProvider authenticationManager, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @Override

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException  {

        String username = request.getHeader("username");
        String password = request.getHeader("password");

       Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
       authenticationManager.authenticate(authentication);

       JwtToken payload = new JwtToken().setUsername(username);

        try {
            String token = jwtTokenService.createToken(objectMapper.writeValueAsString(payload),5L);
            response.setHeader("Authorization",token);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        //we gonna apply the Authentication for only /login path
        return !request.getServletPath().equals("/login");
    }
}
