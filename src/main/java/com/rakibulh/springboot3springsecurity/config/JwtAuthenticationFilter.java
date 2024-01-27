package com.rakibulh.springboot3springsecurity.config;

import com.rakibulh.springboot3springsecurity.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        final String jwt_token = authorizationHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt_token);

        // if we've the user email and the user is not authenticated, we'll validate the user and then the authenticate the user
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // load the user details from the database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // check if the token is valid, if valid then authenticate the user
            if (jwtService.isTokenValid(jwt_token, userDetails)){
                // create the authentication token, this will also set the "isAuthenticated" to "true"
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // set the authentication token in the security context, and now the "getAuthentication()" will hold the authenticated user
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }

}
