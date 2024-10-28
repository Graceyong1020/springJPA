package org.pgm.jpademo.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.pgm.jpademo.domain.Board;
import org.pgm.jpademo.dto.BoardDTO;
import org.pgm.jpademo.dto.PageRequestDTO;
import org.pgm.jpademo.dto.PageResponseDTO;
import org.pgm.jpademo.dto.upload.UploadFileDTO;
import org.pgm.jpademo.dto.upload.UploadResultDTO;
import org.pgm.jpademo.service.BoardService;
import org.pgm.jpademo.service.BoardService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    @Value("${org.pgm.jpademo.upload.path}")
    private String uploadPath;

    private final BoardService boardService;


    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        log.info(responseDTO);
        model.addAttribute("responseDTO", responseDTO);
    }

    @GetMapping("/register")
    public void registerGET() {

    }

    @PostMapping("/register")
    public String registerPost(UploadFileDTO uploadFileDTO,
                               BoardDTO boardDTO,
            /*BindingResult bindingResult, */
                               RedirectAttributes redirectAttributes) {
        List<String> strFileNames = null;
        if (uploadFileDTO.getFiles() != null &&
                !uploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")) { //첨부파일이 있을 때

            strFileNames = fileUpload(uploadFileDTO); //strFileNames가 올린 파일명을 담고 있음

        }
        boardDTO.setFileNames(strFileNames); //boardDTO에 파일명을 저장

        log.info("board POST register.......");
      /*  if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            return "redirect:/board/register";
        }*/
        log.info(boardDTO);
        Long bno = boardService.register(boardDTO);
        //redirectAttributes.addFlashAttribute("result", bno);
        return "redirect:/board/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model) {
        BoardDTO boardDTO = boardService.readOne(bno);
        log.info(boardDTO);
        model.addAttribute("dto", boardDTO);
    }

    @PostMapping("/modify") // remove와 register가 함께 사용하는 메서드
    public String modify(UploadFileDTO uploadFileDTO, PageRequestDTO pageRequestDTO,
                         @Valid BoardDTO boardDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) { //uploadFileDTO: 첨부파일을 받기 위한 객체

        log.info("board modify post......." + boardDTO);

            List<String> strFileNames = null; //파일 가져와서
            if (uploadFileDTO.getFiles() != null &&
                    !uploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")) { //첨부파일이 있을 때

                List<String> fileNames = boardDTO.getFileNames(); //기존 파일명을 가져와서

                if(fileNames != null && fileNames.size() > 0){
                    removeFile(fileNames); //기존 파일 삭제
                }
                strFileNames = fileUpload(uploadFileDTO); //새로운 파일 업로드
                log.info("strFileNames: " + strFileNames.size());
                boardDTO.setFileNames(strFileNames); //새로운 파일로 boardDTO에 저장
            }

            if (bindingResult.hasErrors()) {
            log.info("has errors.......");
            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno", boardDTO.getBno());
            return "redirect:/board/modify?" + link;
        }

        boardService.modify(boardDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/read";
    }

    @PostMapping("/remove")
    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
        log.info("remove post.. " + boardDTO);
        List<String> fileNames = boardDTO.getFileNames();

        if(fileNames != null && fileNames.size() > 0){
            removeFile(fileNames);
        } //첨부파일 삭제는 modify에서 처리

        boardService.remove(boardDTO.getBno()); //여기까지는 게시글 삭제


        redirectAttributes.addFlashAttribute("result", "removed");
        return "redirect:/board/list";
    }


    private List<String> fileUpload(UploadFileDTO uploadFileDTO) {

        List<String> list = new ArrayList<>();
        uploadFileDTO.getFiles().forEach(multipartFile -> {
            String originalName = multipartFile.getOriginalFilename();
            log.info(originalName);

            String uuid = UUID.randomUUID().toString(); //UUID(Universally Unique Identifier)는 범용 고유 식별자
            Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);
            boolean image = false; //이미지 파일인지 아닌지 확인
            try { //try-catch: 이미지 파일이 아닌 경우 예외처리
                multipartFile.transferTo(savePath); //transferTo: 파일을 저장하는 메서드
                if (Files.probeContentType(savePath).startsWith("image")) {
                    image = true;
                    File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName);
                    Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
                } //Thumbnailator.createThumbnail: 이미지 파일을 썸네일로 만드는 메서드 (크기: 200x200)
            } catch (IOException e) {
                e.printStackTrace();
            }
            list.add(uuid + "_" + originalName);

        });
        return list;

    }

    //view 파일 보여주기
    @GetMapping("/view/{fileName}")
    @ResponseBody
    public ResponseEntity<Resource> viewFileGet(@PathVariable("fileName") String fileName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();


        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    private void removeFile(List<String> fileNames) {
        log.info(fileNames.size());

        for (String fileName : fileNames) {
            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
            String resourceName = resource.getFilename();
            //Map<String, Boolean> resultMap = new HashMap<>();
            boolean removed = false;

            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                removed = resource.getFile().delete(); //original file 삭제

                //섬네일이존재한다면
                if (contentType.startsWith("image")) {
                    String fileName1 = fileName.replace("s_", "");
                    File originalFile = new File(uploadPath + File.separator + fileName1);
                    originalFile.delete();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        }
    }

    public String removeFile(@RequestParam("fileName") String fileName){
        log.info(fileName);


        Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);
        String resourceName = resource.getFilename();
        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;
        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();
            //섬네일이존재한다면
            if(contentType.startsWith("image")){
                String fileName1 = fileName.replace("s_", "");
                File originalFile = new File(uploadPath+File.separator + fileName1);
                originalFile.delete();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        resultMap.put("result", removed);
        return "/upload/uploadForm";
    }


}