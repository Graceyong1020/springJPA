package org.pgm.jpademo.service;

import org.pgm.jpademo.domain.Board;
import org.pgm.jpademo.dto.BoardDTO;
import org.pgm.jpademo.dto.PageRequestDTO;
import org.pgm.jpademo.dto.PageResponseDTO;

import java.util.List;

public interface BoardService {
    PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO);
    Board getBoard(Long bno);
    void saveBoard(Board board);
    void updateBoard(Board board);
    void deleteBoard(Long bno);
}
