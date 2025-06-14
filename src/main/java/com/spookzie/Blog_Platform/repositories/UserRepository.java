package com.spookzie.Blog_Platform.repositories;

import com.spookzie.Blog_Platform.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, UUID>
{
    public Optional<User> findByEmail(String email);
}