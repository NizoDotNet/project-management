package com.prjmng.services;

import com.prjmng.entities.User;
import com.prjmng.repositories.UserRepository;
import com.prjmng.shared.DTOs.users.UserResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getOrCreateUser(Jwt jwt) {
        String keycloakId = jwt.getSubject();

        Optional<User> userFromDatabase = userRepository.findByKeycloakId(keycloakId);

        if(userFromDatabase.isPresent()) {
            return new UserResponse(
                    userFromDatabase.get().getId(),
                    userFromDatabase.get().getKeycloakId(),
                    userFromDatabase.get().getEmail(),
                    userFromDatabase.get().getFirstName(),
                    userFromDatabase.get().getLastName());
        }

        User user = User.builder()
                .keycloakId(keycloakId)
                .email(jwt.getClaimAsString("email"))
                .firstName(jwt.getClaimAsString("given_name"))
                .lastName(jwt.getClaimAsString("family_name"))
                .build();

        userRepository.save(user);

        return new UserResponse(
                user.getId(),
                user.getKeycloakId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName());
    }
}
