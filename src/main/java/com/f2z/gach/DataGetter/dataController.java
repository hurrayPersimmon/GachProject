package com.f2z.gach.DataGetter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api/data")
public class dataController {
    private final dataService dataService;

    @PostMapping("/get")
    public dataEntity getData(@RequestBody dataDto datadto) {
        return dataService.getData(datadto);

    }



}
