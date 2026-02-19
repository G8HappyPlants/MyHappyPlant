package com.example.myhappyplants.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.Instant;

@Table(name="blacklisted_tokens")
@Entity
public class BlacklistedJwtToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String tokenId;

	@Column(nullable = false)
	private Instant expiresAt;

	protected BlacklistedJwtToken() {}

	public BlacklistedJwtToken(String tokenId, Instant expiresAt) {
		this.tokenId = tokenId;
		this.expiresAt = expiresAt;
	}

	public Instant getExpiresAt() {
		return expiresAt;
	}

	public String getTokenId() {
		return tokenId;
	}
}
