package com.prjmng.repositories;

import com.prjmng.entities.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardRepository extends  JpaRepository<Board, UUID> {

}
