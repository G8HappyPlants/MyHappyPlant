package com.example.myhappyplants.repository;


import com.example.myhappyplants.entity.BlacklistedJwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<BlacklistedJwtToken, Long> {
    boolean existsByTokenId(String tokenId);
}
