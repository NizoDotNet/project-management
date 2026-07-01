package com.prjmng.services;

import com.prjmng.entities.Board;
import com.prjmng.entities.Project;
import com.prjmng.entities.enums.ProjectMemberRole;
import com.prjmng.repositories.BoardRepository;
import com.prjmng.repositories.ProjectMemberRepository;
import com.prjmng.repositories.ProjectRepository;
import com.prjmng.shared.DTOs.boards.BoardResponse;
import com.prjmng.shared.DTOs.boards.CreateBoardRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;

    public BoardResponse createBoard(@Valid CreateBoardRequest request, UUID userId) {
        boolean userCanCreateBoard = projectMemberRepository.existsByProjectIdAndUserIdAndRoleIn(
                request.getProjectId(),
                userId,
                List.of(ProjectMemberRole.OWNER, ProjectMemberRole.MANAGER)
        );
        if(!userCanCreateBoard) {
            throw new RuntimeException("User cannot create board");
        }

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project with id " + request.getProjectId() + " was not found"));

        Board board = Board.builder()
                .project(project)
                .name(request.getName())
                .type(request.getType())
                .build();
        board = boardRepository.save(board);
        return mapToResponse(board);
    }


    private static @NonNull BoardResponse mapToResponse(Board board) {
        return new BoardResponse(
                board.getId(),
                board.getProject().getId(),
                board.getName(),
                board.getType()
        );
    }
}
