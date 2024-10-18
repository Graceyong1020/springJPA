package org.pgm.jpademo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pgm.jpademo.domain.Board;
import org.pgm.jpademo.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor

public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;


    @Override
    public List<Board> getList() {
        log.info("getList");
        List<Board> boardList = boardRepository.findAll();
        log.info("getList");
        return boardList;
    }
    @Override
    public Board getBoard(Long bno) {
        log.info("getBoard: "+bno);
        return boardRepository.findById(bno).get();
    }

    @Override
    public void saveBoard(Board board) {
        boardRepository.save(board);
        log.info("saveBoard: "+board);
    }

    @Override
    public void updateBoard(Board board) {
        Board oldBoard = boardRepository.findById(board.getBno()).get();
        oldBoard.setTitle(board.getTitle());
        oldBoard.setContent(board.getContent());
        oldBoard.setWriter(board.getWriter());
        boardRepository.save(oldBoard);
        log.info("updateBoard: "+board);
    } /*일부 데이터만 수정할 경우, 해당 데이터만 수정하고 나머지 데이터는 그대로 유지하도록 구현*/

    @Override
    public void deleteBoard(Long bno) {

        boardRepository.deleteById(bno);
        log.info("deleteBoard: "+bno);

    }

}
