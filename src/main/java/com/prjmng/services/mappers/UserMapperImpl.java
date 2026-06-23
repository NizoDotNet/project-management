package com.prjmng.services.mappers;

import com.prjmng.entities.User;
import com.prjmng.shared.DTOs.users.UserResponse;
import com.prjmng.shared.abstractions.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserResponse map(User user) {
        return new UserResponse(user.getId(), user.getKeycloakId(), user.getEmail(), user.getFirstName(), user.getLastName());
    }
}
