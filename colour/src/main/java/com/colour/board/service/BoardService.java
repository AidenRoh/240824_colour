package com.colour.board.service;

import com.colour.board.dto.BoardSearchCond;
import com.colour.board.dto.BoardUpdateDto;
import com.colour.board.entity.Board;

import java.util.List;

public interface BoardService {

    Board createBoard(Board member);
    void updateBoard(Long boardId, BoardUpdateDto dto);
    Board findBoardById(Long boardId);
    List<Board> findAllBoardsByCond(BoardSearchCond cond);
    void deleteBoard(Long boardId);
}
