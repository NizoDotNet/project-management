package com.prjmng.repositories;

import com.prjmng.entities.TicketLabel;
import com.prjmng.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}

