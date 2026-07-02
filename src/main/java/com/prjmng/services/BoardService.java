package com.prjmng.services;

import com.prjmng.entities.Board;
import com.prjmng.entities.BoardColumn;
import com.prjmng.entities.Project;
import com.prjmng.entities.enums.BoardType;
import com.prjmng.entities.enums.ProjectMemberRole;
import com.prjmng.repositories.BoardColumnRepository;
import com.prjmng.repositories.BoardRepository;
import com.prjmng.repositories.ProjectMemberRepository;
import com.prjmng.repositories.ProjectRepository;
import com.prjmng.shared.DTOs.boards.BoardColumnResponse;
import com.prjmng.shared.DTOs.boards.BoardResponse;
import com.prjmng.shared.DTOs.boards.BoardWithColumnsResponse;
import com.prjmng.shared.DTOs.boards.CreateBoardRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardColumnRepository boardColumnRepository;
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

        createDefaultColumns(board);
        board = boardRepository.save(board);
        return mapToResponse(board);
    }

    public BoardWithColumnsResponse getById(UUID id, UUID userId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board with id " + id + " was not found"));

        boolean isUserProjectMember = projectMemberRepository.existsByProjectIdAndUserId(board.getProject().getId(), userId);

        if(!isUserProjectMember) {
            throw new RuntimeException("User with id " + userId + " is not project member");
        }

        List<BoardColumnResponse> boardColumns = boardColumnRepository.findByBoardIdOrderByPosition(id)
                .stream()
                .map(bc -> new BoardColumnResponse(bc.getId(), bc.getName(), bc.getPosition()))
                .toList();

        BoardWithColumnsResponse response = mapToResponse(board, boardColumns);

        return response;
    }

    public Page<BoardResponse> getByProjectId(UUID projectId, UUID userId, PageRequest pageRequest) {
        boolean isUserProjectMember = projectMemberRepository.existsByProjectIdAndUserId(projectId, userId);

        if(!isUserProjectMember) {
            throw new RuntimeException("User with id " + userId + " is not project member");
        }

        Page<Board> boards = boardRepository.findAllByProjectId(projectId, pageRequest);

        return boards.map(b -> mapToResponse(b));
    }

    public void deleteBoard(UUID id, UUID userId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board with id " + id + " was not found"));

        boolean isUserProjectMember = projectMemberRepository.existsByProjectIdAndUserIdAndRoleIn(board.getProject().getId(), userId, List.of(ProjectMemberRole.OWNER, ProjectMemberRole.MANAGER));

        if(!isUserProjectMember) {
            throw new RuntimeException("User with id " + userId + " is not project member");
        }

        boardRepository.delete(board);
    }


    private void createDefaultColumns(Board board) {
        List<String> defaults = board.getType() == BoardType.SCRUM
                ? List.of("Backlog", "To Do", "In Progress", "In Review", "Done")
                : List.of("Backlog", "In Progress", "Done");
        if(board.getBoardColumns() == null) {
            board.setBoardColumns(new ArrayList<>());
        }

        for(int i = 0; i < defaults.size(); i++) {
            BoardColumn column = BoardColumn
                    .builder()
                    .name(defaults.get(i))
                    .position(i)
                    .build();
            board.addColumn(column);
        }
    }
    private static @NonNull BoardWithColumnsResponse mapToResponse(Board board, List<BoardColumnResponse> boardColumns) {
        return new BoardWithColumnsResponse(board.getId(), board.getProject().getId(), board.getName(), boardColumns, board.getType());
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
