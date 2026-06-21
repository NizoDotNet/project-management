package com.prjmng.repositories;

import com.prjmng.entities.BoardColumn;
import com.prjmng.entities.Label;
import com.prjmng.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LabelRepository extends  JpaRepository<Label, UUID> {

}


