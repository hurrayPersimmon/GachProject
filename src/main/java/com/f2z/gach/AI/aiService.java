package com.f2z.gach.AI;

import lombok.extern.slf4j.Slf4j;
import org.pytorch.Module;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class aiService {

    @GetMapping("/test")
    public void test(String[] args){
        aiTest ai = new aiTest();
        ai.module = Module.load("first_model.pt");
        float[] inputData = {
                1715191742, 1999, 0, 178, 1.0f, 1.0f, 2.0f, 3.0f, 1.0f, 2.0f
        };
        log.info(ai.toString());
    }
}
