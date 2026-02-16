package com.example.myhappyplants.repository;


import com.example.myhappyplants.entity.BlacklistedJwtToken;
import com.example.myhappyplants.entity.UserPlant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TokenBlacklistRepository extends JpaRepository<BlacklistedJwtToken, Long> {
	List<UserPlant> findByTokenId(String tokenId);
}
