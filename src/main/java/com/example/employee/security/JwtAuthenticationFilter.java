package com.example.employee.security;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

   
     private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    private final AuthenticationEntryPoint authenticationEntryPoint;


    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService,AuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
                String authHeader=request.getHeader("Authorization");

                if(authHeader==null || !authHeader.startsWith("Bearer ")){
                    filterChain.doFilter(request, response);
                    return;
                }

                String jwt=authHeader.substring(7).trim();

                 try {

            String username = jwtService.extractUsername(jwt);

            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails =
                        customUserDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (ExpiredJwtException e) {

    authenticationEntryPoint.commence(
            request,
            response,
            new BadCredentialsException("JWT token has expired", e)
    );

    return;
}

catch (JwtException | IllegalArgumentException e) {

    authenticationEntryPoint.commence(
            request,
            response,
            new BadCredentialsException("Invalid JWT token", e)
    );

    return;
}
        filterChain.doFilter(request, response);
                

            }
        }