package com.prjmng.repositories;

import com.prjmng.entities.Milestone;
import com.prjmng.entities.Notification;
import com.prjmng.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends  JpaRepository<Notification, UUID> {

}

