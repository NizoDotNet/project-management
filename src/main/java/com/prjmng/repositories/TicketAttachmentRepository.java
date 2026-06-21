package com.prjmng.repositories;

import com.prjmng.entities.TicketAssignee;
import com.prjmng.entities.TicketAttachment;
import com.prjmng.entities.TicketLabel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketAttachmentRepository extends  JpaRepository<TicketAttachment, UUID> {

}


