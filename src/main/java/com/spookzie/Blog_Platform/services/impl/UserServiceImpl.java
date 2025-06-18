package com.spookzie.Blog_Platform.services.impl;

import com.spookzie.Blog_Platform.domain.entities.User;
import com.spookzie.Blog_Platform.repositories.UserRepository;
import com.spookzie.Blog_Platform.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepo;


    @Override
    public User getUserById(UUID id)
    {
        return this.userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }
}