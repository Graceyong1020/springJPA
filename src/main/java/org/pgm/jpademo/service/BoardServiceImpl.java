package org.pgm.jpademo.service;

import jakarta.transaction.Transactional;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService{
    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;


    @Override
    public Long register(BoardDTO boardDTO) {
        //Board board = modelMapper.map(boardDTO, Board.class);
        Board board = dtoToEntity(boardDTO);
        Long bno = boardRepository.save(board).getBno();
        return bno;
    }

    @Override
    public BoardDTO readOne(Long bno) {
        //Optional<Board> result = boardRepository.findById(bno);
        Optional<Board> result = boardRepository.findByIdWithImages(bno); //findByIdWithImages 메서드를 이용하여 게시글과 이미지 정보를 함께 조회
        Board board = result.orElseThrow();
        //BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
        BoardDTO boardDTO= entityToDTO(board);
        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        Optional<Board> result = boardRepository.findById(boardDTO.getBno());
        Board board = result.orElseThrow();

        board.change(boardDTO.getTitle(), boardDTO.getContent());
        board.clearImages();
        if(boardDTO.getFileNames()!=null) {
            for (String fileName : boardDTO.getFileNames()) {
                String[] arr=fileName.split("_");
                board.addImage(arr[0], arr[1]);
            }
        }
        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<Board> result = boardRepository.searchAll(types,keyword, pageable);

        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board,BoardDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }
}