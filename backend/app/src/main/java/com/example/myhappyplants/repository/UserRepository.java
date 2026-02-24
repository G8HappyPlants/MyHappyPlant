package com.example.myhappyplants.repository;

import com.example.myhappyplants.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailHash(String emailHash);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
