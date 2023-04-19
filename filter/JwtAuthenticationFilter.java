package com.go.tokenverification.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.go.tokenverification.entity.UserEntity;
import com.go.tokenverification.jwt.JwtExpiredException;
import com.go.tokenverification.jwt.JwtToken;
import com.go.tokenverification.jwt.JwtTokenService;
import com.go.tokenverification.model.security.User;
import com.go.tokenverification.service.UserService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final RequestMatcher REQUESTMATCHER = new NegatedRequestMatcher(new OrRequestMatcher(Arrays.asList(
            new AntPathRequestMatcher("/login"),
            new AntPathRequestMatcher("/signUp"),
            new AntPathRequestMatcher("/email/verification"))));


    private final JwtTokenService jwtTokenService;

    private final UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${token.expire.minutes}")
    private String tokenExpireTime;

    private ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(JwtTokenService jwtTokenService, UserService userService) {
        this.jwtTokenService = jwtTokenService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt  = request.getHeader("Authorization");
        try {

            JwtToken jwtToken = objectMapper.readValue(jwtTokenService.getPayloadToken(jwt, Long.parseLong(tokenExpireTime)),
                    JwtToken.class);
            User user = userService.loadUserByUsername(jwtToken.getUsername());
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        } catch (JwtExpiredException e) {
            throw new RuntimeException(e);
        }
        filterChain.doFilter(request, response);


    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        //we will not using JwtAuthenticationFilter for path /login and /signUp
        return !REQUESTMATCHER.matches(request);
    }
}
