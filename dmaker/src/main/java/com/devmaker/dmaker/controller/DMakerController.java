package com.devmaker.dmaker.controller;

import com.devmaker.dmaker.service.DmakerService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor // 자동으로 생성자를 만들어 Spring Application Context 위에 주입
@Slf4j
@RestController
// @RestController: @Controller 와 @ResponseBody 를 함께 달아주는 annotation
// 사용자 요청을 받아 Json 형태로 응답을 내려준다.
public class DMakerController {
    // service 빈 주입, Spring Application Context 위에 주입
    private final DmakerService dmakerService;


    @GetMapping("/developers")
    public List<String> getAllDevelopers() {
        // GET / developers HTTP/1.1
        log.info("GET / developers HTTP/1.1");

        return Arrays.asList("snow", "Elsa", "Olaf");
    }

    @GetMapping("/create-developers")
    public List<String> createDevelopers() {
        // GET / developers HTTP/1.1
        log.info("GET / developers HTTP/1.1");

        dmakerService.createDeveloper();

        return Collections.singletonList("Kim");
    }

}
