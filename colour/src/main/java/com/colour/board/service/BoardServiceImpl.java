package com.colour.board.service;

import com.colour.board.dto.BoardSearchCond;
import com.colour.board.dto.BoardUpdateDto;
import com.colour.board.entity.Board;
import com.colour.board.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.repository = boardRepository;
    }

    @Override
    public Board createBoard(Board board) {
        return repository.create(board);
    }

    @Override
    public void updateBoard(Long boardId, BoardUpdateDto dto) {
        repository.update(boardId, dto);
    }

    @Override
    public Board findBoardById(Long boardId) {
        return repository.findById(boardId).orElse(null);
    }

    @Override
    public List<Board> findAllBoardsByCond(BoardSearchCond cond) {
        return List.of();
    }

    @Override
    public void deleteBoard(Long boardId) {
        repository.delete(boardId);
    }
}
