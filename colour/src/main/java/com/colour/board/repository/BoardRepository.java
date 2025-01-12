package com.colour.board.repository;

import com.colour.board.dto.BoardSearchCond;
import com.colour.board.dto.BoardUpdateDto;
import com.colour.board.entity.Board;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository {

    Board create(Board board);
    Optional<Board> findById(Long boardId);
    List<Board> findAll(BoardSearchCond boardCond);
    void update(Long boardId, BoardUpdateDto updateDto);
    void delete(Long boardId);
}
