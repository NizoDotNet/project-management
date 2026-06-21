package com.prjmng.repositories;

import com.prjmng.entities.Label;
import com.prjmng.entities.Milestone;
import com.prjmng.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MilestoneRepository extends JpaRepository<Milestone, UUID> {

}


