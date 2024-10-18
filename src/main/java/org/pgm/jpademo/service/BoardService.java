package org.pgm.jpademo.service;

import org.pgm.jpademo.domain.Board;

import java.util.List;

public interface BoardService {
    List<Board> getList();


    Board getBoard(Long bno);

    void saveBoard(Board board);
    void updateBoard(Board board);
    void deleteBoard(Long bno);
}
