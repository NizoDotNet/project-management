package com.prjmng.repositories;

import com.prjmng.entities.TeamMember;
import com.prjmng.entities.Ticket;
import com.prjmng.entities.TicketLabel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends  JpaRepository<Ticket, UUID> {

}


