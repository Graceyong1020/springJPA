package org.pgm.jpademo.service;

import org.pgm.jpademo.domain.Board;
import org.pgm.jpademo.dto.BoardDTO;
import org.pgm.jpademo.dto.PageRequestDTO;
import org.pgm.jpademo.dto.PageResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {
    Long register(BoardDTO boardDTO);
    BoardDTO readOne(Long bno);
    void modify(BoardDTO boardDTO);
    void remove(Long bno);
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    default Board dtoToEntity(BoardDTO boardDTO){ //boardDTO로 부터 가져온 정보를 entity로 변환
        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();
        if(boardDTO.getFileNames() != null){ //파일이름이 존재하면
            boardDTO.getFileNames().forEach(fileName -> {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]); //uuid와 파일이름을 이용하여 이미지를 추가, arr[0]은 uuid, arr[1]은 파일이름
            });
        }
        return board;
    }
    default BoardDTO entityToDTO(Board board) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();

        List<String> fileNames =
                board.getImageSet().stream().sorted().map(boardImage ->
                                boardImage.getUuid()+"_"+boardImage.getFileName())
                        .collect(Collectors.toList());
        boardDTO.setFileNames(fileNames);
        return boardDTO;
    }

}
