package com.prjmng.repositories;

import com.prjmng.entities.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BoardColumnRepository extends  JpaRepository<BoardColumn, UUID> {
    List<BoardColumn> findByBoardIdOrderByPosition(UUID id);
    @Modifying
    @Query("""
        UPDATE BoardColumn i
        SET i.position = i.position + 1
        WHERE i.position >= :from
          AND i.position <= :to
    """)
    void incrementPositionsBetween(int from, int to);
    @Modifying
    @Query("""
        UPDATE BoardColumn i
        SET i.position = i.position - 1
        WHERE i.position >= :from
          AND i.position <= :to
    """)
    void decrementPositionsBetween(int from, int to);
}


