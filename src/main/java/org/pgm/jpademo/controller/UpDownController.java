package org.pgm.jpademo.controller;


import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.pgm.jpademo.dto.BoardDTO;
import org.pgm.jpademo.dto.upload.UploadFileDTO;
import org.pgm.jpademo.dto.upload.UploadResultDTO;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files; //java.nio.file.Files는 파일을 다루는데 사용되는 클래스
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@Log4j2
@RequestMapping("/upload")

public class UpDownController {

    @Value("${org.pgm.jpademo.upload.path}")
    private String uploadPath;

    @GetMapping("/uploadForm")
    public void uploadForm() {

    }

    @PostMapping(value = "/uploadPro")
    public void uploadPro(UploadFileDTO uploadFileDTO, BoardDTO boardDTO, Model model) {

        //log.info(uploadFileDTO);
        log.info(boardDTO);

        if (uploadFileDTO.getFiles() != null) {
            final List<UploadResultDTO> list = new ArrayList<>();

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
                list.add(UploadResultDTO.builder() //UploadResultDTO에 저장해서 model에 전달
                        .uuid(uuid)
                        .filename(originalName)
                        .image(image)
                        .build()
                );
                model.addAttribute("list", list);
                model.addAttribute("uploadPath", uploadPath);
            });
        }

    }

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
    @GetMapping("/remove")
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