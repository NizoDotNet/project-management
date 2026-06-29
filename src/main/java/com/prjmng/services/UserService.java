package com.prjmng.services;

import com.prjmng.entities.User;
import com.prjmng.repositories.UserRepository;
import com.prjmng.shared.DTOs.users.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public Page<UserResponse> getAllWithPagination(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest).map(u -> new UserResponse(u.getId(), u.getKeycloakId(), u.getEmail(), u.getFirstName(), u.getLastName()));
    }
}
