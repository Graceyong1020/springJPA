package org.pgm.jpademo.controller;

import lombok.extern.log4j.Log4j2;
import org.pgm.jpademo.dto.SampleDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Log4j2
public class SampleController {

    @GetMapping("/hello")
    public void hello(Model model) {
        log.info("hello");
        model.addAttribute("name", "Spring");
    }
    @GetMapping("/hello1")
    public void hello1(@RequestParam("name") String name,
                       @RequestParam("age") int age,
                       Model model) {
        log.info("hello1");
        model.addAttribute("name", "Spring");
    }
    @GetMapping("/hello2")
    public void hello2(Model model) {
        log.info("hello2");
        model.addAttribute("name", "Spring");
    }
    @GetMapping("/hello3")
    public void hello3(Model model) {
        log.info("hello3");
        model.addAttribute("name", "Spring");
    }
    @GetMapping("/ex/ex1")
    public void ex1(Model model) {
        log.info("ex1");
        List<String> list = Arrays.asList("AAA", "BBB", "CCC", "DDD");
        model.addAttribute("list", list);
    }
    @GetMapping("/ex/ex2")
    public void ex2(Model model) {
        log.info("ex2");
        List<String> strList2=new ArrayList<>();
        List<String> strList = IntStream.range(1,10)
                .mapToObj(i-> "Data" + i)
                .collect(Collectors.toList());
        for(int i=1; i<10; i++) {
            strList2.add("Data" + i);

        }

        // Map으로 데이터 전달
        Map<String, Integer> maps = new HashMap<>();
        maps.put("HKD", 100);
        maps.put("JPY", 46);
        maps.put("CNY", 15);
        model.addAttribute("maps", maps);

        // DTO로 데이터 전달
        SampleDTO sampleDTO = new SampleDTO();
        sampleDTO.setName("Hong");
        sampleDTO.setAge(10);
        sampleDTO.setGender("M");
        model.addAttribute("sampleDTO", sampleDTO);


        model.addAttribute("strList", strList);
        model.addAttribute("strList2", strList2);
    }
    @GetMapping("/ex/ex3")
    public void ex3(Model model) {
        log.info("ex3");
    }

}
