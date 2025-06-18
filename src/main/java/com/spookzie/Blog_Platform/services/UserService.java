package com.spookzie.Blog_Platform.services;

import com.spookzie.Blog_Platform.domain.entities.User;

import java.util.UUID;


public interface UserService
{
    User getUserById(UUID id);
}