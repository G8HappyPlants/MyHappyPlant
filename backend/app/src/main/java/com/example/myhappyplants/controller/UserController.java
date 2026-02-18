package com.example.myhappyplants.controller;

import com.example.myhappyplants.entity.User;
import com.example.myhappyplants.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserRepository userRepository;

	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public String retrieveMe(@AuthenticationPrincipal UserDetails selfUser) {
		return selfUser.getUsername(); // email
	}

	/**
	 * Deletes the current authenticated user
	 *
	 * @return
	 */
	@DeleteMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Void> deleteMe(@AuthenticationPrincipal UserDetails selfUser) {
		User user = userRepository.findByEmail(selfUser.getUsername())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"User entry not found"));

        userRepository.delete(user);

		return ResponseEntity.noContent().build();
	}
}