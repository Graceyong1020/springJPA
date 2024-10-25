package org.pgm.jpademo.service;

import org.pgm.jpademo.domain.Board;
import org.pgm.jpademo.dto.BoardDTO;
import org.pgm.jpademo.dto.PageRequestDTO;
import org.pgm.jpademo.dto.PageResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService1 { //인터페이스는 함수의 모양을 정해주고 구현은 다른 클래스에서, 단, default 함수는 구현 가능
    PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO);


    Board getBoard(Long bno);

    void saveBoard(Board board);

    void updateBoard(Board board);

    void deleteBoard(Long bno);

    default Board dtoToEntity(BoardDTO boardDTO) {
        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();

        if (boardDTO.getFileNames() != null) { //DTO를 엔티티로 변환할 때 이미지 파일이 있으면 이미지 파일을 추가
            boardDTO.getFileNames().forEach(fileName -> { //forEach를 이용하여 파일명을 하나씩 꺼내서 저장
                String[] arr = fileName.split("_"); //파일명을 _로 구분하여 배열로 저장
                board.addImage(arr[0], arr[1]); //addImage 함수를 이용하여 이미지를 추가
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

        List<String> fileNames = board.getImageSet()//이미지 파일명을 가져옴
                .stream()
                .sorted()
                .map(boardImage ->
                        boardImage.getUuid() + "_" + boardImage.getFileName())
                .collect(Collectors.toList());
        boardDTO.setFileNames(fileNames);
        return boardDTO;
    }


}
