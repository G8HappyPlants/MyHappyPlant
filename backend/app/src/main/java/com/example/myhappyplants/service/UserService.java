package com.example.myhappyplants.service;

import com.example.myhappyplants.entity.User;
import com.example.myhappyplants.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final CryptoService cryptoService;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, CryptoService cryptoService) {
        this.userRepository = userRepository;
        this.cryptoService = cryptoService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailHash(cryptoService.hash(email.trim().toLowerCase()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    public void removeAccountByUserDetails(UserDetails request) {
        User user = userRepository.findByEmailHash(cryptoService.hash(request.getUsername()))
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"User entry not found"));

        userRepository.delete(user);
    }
}
