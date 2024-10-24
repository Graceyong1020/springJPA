package org.pgm.jpademo.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.pgm.jpademo.domain.Board;
import org.pgm.jpademo.dto.BoardDTO;
import org.pgm.jpademo.dto.PageRequestDTO;
import org.pgm.jpademo.dto.PageResponseDTO;
import org.pgm.jpademo.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    @Autowired //의존성 주입
    private BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) { //model은 데이터를 담아서 전달하는 역할
        PageResponseDTO<BoardDTO> responseDTO = boardService.getList(pageRequestDTO);
        log.info(responseDTO);
       /* model.addAttribute("responseDTO", boardService.getList(pageRequestDTO));*/
        model.addAttribute("responseDTO", responseDTO);

    }
    @GetMapping("/register")
    public void registerGet() {
        log.info("registerGet");
    }

    @PostMapping("/register")
    public String registerPost(Board board) {
        log.info("registerPost");
        boardService.saveBoard(board);
        return "redirect:/board/list"; // redirection은 다시 controller를 선택하라는 의미
    }
    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model) {
        log.info("read"+bno);
        model.addAttribute("dto", boardService.getBoard(bno));
    }
    @PostMapping("/modify")
    public String modify(Board board, PageRequestDTO pageRequestDTO,
                         RedirectAttributes redirectAttributes) {
        log.info("modifyPost" + board);
        boardService.updateBoard(board);
        redirectAttributes.addAttribute("bno", board.getBno());
        return "redirect:/board/read";
    }
    @PostMapping("/remove")
    public String remove(Board board) {
        log.info("remove" + board);
        boardService.deleteBoard(board.getBno());
        return "redirect:/board/list";
    }


}
