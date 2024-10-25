package org.pgm.jpademo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.pgm.jpademo.domain.Board;
import org.pgm.jpademo.dto.BoardDTO;
import org.pgm.jpademo.dto.PageRequestDTO;
import org.pgm.jpademo.dto.PageResponseDTO;
import org.pgm.jpademo.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor

public class BoardServiceImpl1 implements BoardService1 {
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper; //ModelMapper를 사용하여 Entity와 DTO간의 변환을 처리


    @Override
    public PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO) {
        log.info("getList");
       /* Pageable pageable = pageRequestDTO.getPageable("bno");
       // Page<Board> result = boardRepository.findAll(pageable); // page정보와 정렬정보를 이용하여 페이징 처리된 결과를 가져온다
        Page<Board> result = null;
        if(pageRequestDTO.getKeyword()==null || pageRequestDTO.getKeyword().equals("")){
           result = boardRepository.findAll(pageable);
        }else {
            result = boardRepository.searchAll(pageRequestDTO.getKeyword(), pageable);
        }
        log.info("aaa"+result.getTotalElements());
*/
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
        List<BoardDTO> dtoList = result.getContent().stream().map(board
                -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toUnmodifiableList()); //Board를 BoardDTO로 변환

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public Board getBoard(Long bno) {
        log.info("getBoard: "+bno);
        Board board = boardRepository.findById(bno).get();
        board.updateVisitCount();
        boardRepository.save(board);
        return board;
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
