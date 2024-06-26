package com.f2z.gach.DataGetter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getDatabase")
    public void getDatabase() {
        dataService.getDatabases();
    }

}
