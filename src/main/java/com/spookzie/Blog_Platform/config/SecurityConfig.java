package com.spookzie.Blog_Platform.config;

import com.spookzie.Blog_Platform.domain.entities.User;
import com.spookzie.Blog_Platform.repositories.UserRepository;
import com.spookzie.Blog_Platform.security.BlogUserDetailsService;
import com.spookzie.Blog_Platform.security.JwtAuthenticationFilter;
import com.spookzie.Blog_Platform.services.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig
{
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationService auth_service)
    {
        return new JwtAuthenticationFilter(auth_service);
    }


    @Bean
    public UserDetailsService userDetailsService(UserRepository user_repo)
    {
        BlogUserDetailsService blogUserDetailsService = new BlogUserDetailsService(user_repo);

        /*  Saving a default test user  */
        String email = "user@test.com";
        user_repo.findByEmail(email).orElseGet(() ->{
            User newUser = User.builder()
                    .name("Test User 4")
                    .email(email)
                    .password(this.passwordEncoder().encode("password"))
                    .build();

            return user_repo.save(newUser);
        });


        return blogUserDetailsService;
    }


    /*
    * HttpSecurity - fluent API used to configure what endpoints are secured or public, what kind of authentication is used, session policy, CSRF, etc.
    *
    * Allowing certain GET requests to be accessed by anyone while all other request require authentication
    * Disabling CSRF protection, not necessary since we are using stateless REST APIs, and we don't use sessions or cookies.
    * Creating a stateless session - server will not store any session; every request must carry authentication on its own
    *******************************/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwt_auth_filter) throws Exception
    {
        http.authorizeHttpRequests(
                auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/drafts").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/tags/**").permitAll()
                        .anyRequest().authenticated()
                ).csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwt_auth_filter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }


    /*  DelegatingPasswordEncoder supports multiple encodings   */
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    /*  Retrieving the default AuthenticationManager from Spring's Configuration for custom login/authentication logic  */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }
}