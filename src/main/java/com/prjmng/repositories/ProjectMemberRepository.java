package com.prjmng.repositories;

import com.prjmng.entities.Project;
import com.prjmng.entities.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectMemberRepository extends  JpaRepository<ProjectMember, UUID> {

}

