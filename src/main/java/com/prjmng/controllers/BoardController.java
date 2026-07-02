package com.prjmng.controllers;

import com.prjmng.entities.User;
import com.prjmng.services.BoardService;
import com.prjmng.services.UserService;
import com.prjmng.shared.DTOs.boards.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<BoardResponse> create(
            @Valid @RequestBody CreateBoardRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        User user = userService.getOrCreateUser(jwt);
        BoardResponse response = boardService.createBoard(request, user.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardWithColumnsResponse> getById(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {

        User user = userService.getOrCreateUser(jwt);
        BoardWithColumnsResponse response = boardService.getById(id, user.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<BoardResponse>> getByProjectId(
            @RequestParam UUID projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @AuthenticationPrincipal Jwt jwt) {

        User user = userService.getOrCreateUser(jwt);

        Page<BoardResponse> response = boardService.getByProjectId(
                projectId,
                user.getId(),
                PageRequest.of(page, pageSize)
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt) {

        User user = userService.getOrCreateUser(jwt);
        boardService.deleteBoard(id, user.getId());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("{id}/columns")
    public ResponseEntity<BoardColumnResponse> createColumn(
            @PathVariable UUID id,
            @Valid @RequestBody CreateBoardColumnRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);
        BoardColumnResponse response = boardService.createBoardColumn(id, request, user.getId());
        return  ResponseEntity.ok(response);
    }

    @DeleteMapping("{boardId}/columns/{columnId}")
    public ResponseEntity<Void> createColumn(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @AuthenticationPrincipal Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);
        boardService.deleteBoardColumn(columnId, user.getId());
        return  ResponseEntity.noContent().build();
    }

    @PutMapping("{id}/columns/{columnId}")
    public ResponseEntity<Void> update(
            @PathVariable UUID id,
            @PathVariable UUID columnId,
            @Valid @RequestBody UpdateBoardColumnRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        User user = userService.getOrCreateUser(jwt);
        boardService.updateBoardColumn(columnId, request, user.getId());
        return ResponseEntity.noContent().build();
    }

}