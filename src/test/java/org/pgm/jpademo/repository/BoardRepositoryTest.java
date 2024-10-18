package org.pgm.jpademo.repository;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.pgm.jpademo.domain.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Log4j2
public class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testInsert() {
        Board board = new Board();
        board.setTitle("title");
        board.setWriter("writer");
        board.setContent("content");
        //boardRepository.save(board);


        Board board1 = Board.builder()
                .title("title1")
                .content("content1")
                .writer("writer1")
                .build();
        boardRepository.save(board1);


    }

    @Test
    public void getListTest() {
        List<Board> list = boardRepository.findAll();
        for (Board board : list) {
            log.info(board);
        }

    }

    @Test
    public void getOneTest() {
        Board board = boardRepository.findById(1L).get();
        log.info(board);
    }

    @Test
    public void updateBoardTest() {
        Board board = boardRepository.findById(1L).get();
        board.setTitle("title2");
        board.setContent("content2");
        boardRepository.save(board);
    } //수정: update 할때 save() 메서드를 이용하여 수정할 수 있음.
    // 기존의 원래 데이터를 가져와서 수정하고 save() 메서드를 이용하여 저장

    @Test
    public void deleteBoardTest() {
        boardRepository.deleteById(1L);
    }

}
