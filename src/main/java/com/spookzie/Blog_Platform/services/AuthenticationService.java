package com.spookzie.Blog_Platform.services;

import org.springframework.security.core.userdetails.UserDetails;


public interface AuthenticationService
{
    UserDetails authenticate(String email, String password);

    String generateToken(UserDetails user_details);

    UserDetails validateToken(String token);
}