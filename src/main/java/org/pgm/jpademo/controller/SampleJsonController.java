package org.pgm.jpademo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class SampleJsonController {

    @GetMapping("/hello10")
    public String hello(){

        return "Hello";
    }

    @GetMapping("/helloArr10")
    public String[] helloArr(){

        log.info("helloArr..................");

        return new String[]{"hello","world"};
    }
}
