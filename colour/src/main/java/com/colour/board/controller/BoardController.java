package com.colour.board.controller;

import com.colour.board.dto.BoardCreateDto;
import com.colour.board.dto.BoardUpdateDto;
import com.colour.board.entity.Board;
import com.colour.board.service.BoardService;
import com.colour.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board/test")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;

    @GetMapping("create-board")
    public String createBoard() {
        return "create-board";
    }

    @PostMapping("create-board/{member_id}")
    public String createBoard(@RequestBody BoardCreateDto dto,
                              @PathVariable Long member_id) {
        String writer = memberService.findMemberById(member_id).getUsername();
        Board board = new Board(writer, dto.getTitle(), dto.getContent());
        boardService.createBoard(board);
        return "create-board";
    }

    @GetMapping("update-board")
    public String updateBoard() {
        return "updated-board";
    }

    @PatchMapping("update-board/{member_id}/{board_id}")
    public String updateBoard(@RequestBody BoardUpdateDto dto,
                              @PathVariable Long member_id, @PathVariable Long board_id) {
        if (doesWriterRequest(member_id, board_id)) {
            boardService.updateBoard(board_id, dto);
        }
        return "updated-board";
    }

    @GetMapping("get-boards")
    public String getBoards() {
        return "get-boards";
    }

    @DeleteMapping("delete-board/{member_id}/{board_id}")
    public String deleteBoard(@PathVariable Long member_id, @PathVariable Long board_id) {
        if (doesWriterRequest(member_id, board_id)) {
            boardService.deleteBoard(board_id);
        }
        return "delete-board";
    }

    private boolean doesWriterRequest(Long member_id, Long board_id) {
        Board board = boardService.findBoardById(board_id);
        return board.getWriter().equals(memberService.findMemberById(member_id).getUsername());
    }
}

