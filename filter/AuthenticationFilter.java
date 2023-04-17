package com.go.tokenverification.filter;

import com.go.tokenverification.jwt.JwtTokenHelper;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private  JwtTokenHelper jwtTokenHelper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException  {

        String username = request.getHeader("username");
        String password = request.getHeader("password");

       Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
       authenticationManager.authenticate(authentication);

        try {
            String token = jwtTokenHelper.createToken(authentication.toString(),5L);
            response.setHeader("Authorization",token);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        //we gonna apply the InitialAuthenticationFilter for only /login path
        return !request.getServletPath().equals("/login");
    }
}
