package com.f2z.gach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class GachApplication {

    public static void main(String[] args) {
        SpringApplication.run(GachApplication.class, args);
    }

}
