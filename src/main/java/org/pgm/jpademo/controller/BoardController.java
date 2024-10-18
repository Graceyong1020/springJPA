package org.pgm.jpademo.controller;


import lombok.extern.log4j.Log4j2;
import org.pgm.jpademo.domain.Board;
import org.pgm.jpademo.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@Controller
@RequestMapping("/board")
public class BoardController {
    @Autowired //의존성 주입
    private BoardService boardService;

    @GetMapping("/list")
    public void list(Model model) { //model은 데이터를 담아서 전달하는 역할
        log.info("controller list");
        model.addAttribute("boardList", boardService.getList());

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
    @GetMapping("/read")
    public void read(@RequestParam("bno") Long bno, Model model) {
        log.info("read");
        Board board = boardService.getBoard(bno);
        model.addAttribute("board", board);
    } //read와 modify는 같은 화면을 사용하기 때문에 같은 메서드를 사용


}
