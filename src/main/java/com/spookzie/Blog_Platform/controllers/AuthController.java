package com.spookzie.Blog_Platform.controllers;

import com.spookzie.Blog_Platform.domain.dtos.AuthResponse;
import com.spookzie.Blog_Platform.domain.dtos.LoginRequest;
import com.spookzie.Blog_Platform.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/api/v1/auth/login")
@RequiredArgsConstructor
public class AuthController
{
    private final AuthenticationService authService;


    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest login_request)
    {
        UserDetails userDetails = this.authService.authenticate(
                login_request.getEmail(),
                login_request.getPassword()
        );

        String token = this.authService.generateToken(userDetails);

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .expiresIn(86400)   // 24 hrs in seconds
                .build();


        return ResponseEntity.ok(authResponse);
    }
}