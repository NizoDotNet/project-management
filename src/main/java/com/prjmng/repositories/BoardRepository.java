package com.prjmng.repositories;

import com.prjmng.entities.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardRepository extends  JpaRepository<Board, UUID> {
    Page<Board> findAllByProjectId(UUID projectId, Pageable pageable);
}
