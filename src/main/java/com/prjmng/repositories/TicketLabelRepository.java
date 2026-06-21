package com.prjmng.repositories;

import com.prjmng.entities.TicketHistory;
import com.prjmng.entities.TicketLabel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketLabelRepository extends  JpaRepository<TicketLabel, UUID> {
    
}

