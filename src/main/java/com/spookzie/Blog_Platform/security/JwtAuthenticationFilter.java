package com.spookzie.Blog_Platform.security;

import com.spookzie.Blog_Platform.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/*
* Custom Spring Security filter that:
* - Intercepts each HTTP request (OncePerRequestFilter)
* - Extracts the JWT from the request
* - Validates the token
* - If valid, sets the user as authenticated in Spring’s SecurityContext
* - Then lets the request continue
**********************************/
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    private final AuthenticationService authService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        try
        {
            String token = this.extractToken(request);
            if(token != null)
            {
                UserDetails userDetails = this.authService.validateToken(token);

                // Creating the authentication object manually
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,    // Principal - user identity
                        null,           // Credentials - user password/token
                        userDetails.getAuthorities()    // Authorities - roles/permissions (in this case only ROLE_USER)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Storing the user id into the HTTPServletRequest so controllers/services can access later (eg GET drafts)
                if(userDetails instanceof BlogUserDetails)
                    request.setAttribute("user_id", ((BlogUserDetails) userDetails).getId());
            }
        } catch (Exception ex) {
            /* Do not throw exceptions, just don't authenticate the user    */
            log.warn("Received invalid auth token");
        }

        filterChain.doFilter(request, response);
    }


    /*
    * Ensuring that only "Bearer " headers are accepted and the rest are skipped
    * Bearer tokens are standard in JWT-based stateless authentication
    ************************/
    private String extractToken(HttpServletRequest request)
    {
        String bearerToken = request.getHeader("Authorization");

        if(bearerToken != null && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);


        return null;
    }
}