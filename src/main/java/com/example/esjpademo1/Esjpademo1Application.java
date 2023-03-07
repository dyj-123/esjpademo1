package com.example.esjpademo1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class Esjpademo1Application {

    public static void main(String[] args) {

        System.setProperty("es.set.netty.runtime.available.processors","false");
        SpringApplication.run(Esjpademo1Application.class, args);
    }

}
