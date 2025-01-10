package com.colour.board.repository;

import com.colour.board.dto.BoardCond;
import com.colour.board.dto.BoardUpdateDto;
import com.colour.board.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {

    Board create(Board board);
    Optional<Board> findById(Long boardId);
    List<Board> findAll(BoardCond boardCond);
    void update(Long boardId, BoardUpdateDto updateDto);
    void delete(Long boardId);
}
