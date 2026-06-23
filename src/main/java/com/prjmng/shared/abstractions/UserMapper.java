package com.prjmng.shared.abstractions;

import com.prjmng.entities.User;
import com.prjmng.shared.DTOs.users.UserResponse;

public interface UserMapper {
    UserResponse map(User user);
}
