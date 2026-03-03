package com.example.myhappyplants.service;

import com.example.myhappyplants.entity.User;
import com.example.myhappyplants.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final CryptoService cryptoService;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, CryptoService cryptoService) {
        this.userRepository = userRepository;
        this.cryptoService = cryptoService;
    }

    public User loadUserByEmail(String email) {
        return userRepository.findByEmailHash(cryptoService.hash(email.trim().toLowerCase()))
                .orElse(null);
    }

    public User loadUserByUserDetails(Authentication user) {
        return loadUserByEmail(user.getName());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable(loadUserByEmail(email))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User entry not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    public boolean removeAccountByUserDetails(UserDetails request) {
        Optional<User> user = userRepository.findByEmailHash(cryptoService.hash(request.getUsername()));
        user.ifPresent(userRepository::delete);

        return user.isPresent();
    }
}
