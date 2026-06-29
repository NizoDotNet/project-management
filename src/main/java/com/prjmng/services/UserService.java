package com.prjmng.services;

import com.prjmng.entities.User;
import com.prjmng.repositories.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User getOrCreateUser(Jwt jwt) {
        String keycloakId = jwt.getSubject();

        Optional<User> userFromDatabase = userRepository.findByKeycloakId(keycloakId);

        if(userFromDatabase.isPresent()) {
            return userFromDatabase.get();
        }

        User user = User.builder()
                .keycloakId(keycloakId)
                .email(jwt.getClaimAsString("email"))
                .firstName(jwt.getClaimAsString("given_name"))
                .lastName(jwt.getClaimAsString("family_name"))
                .build();

        User savedUser = userRepository.save(user);

        return savedUser;
    }
}
