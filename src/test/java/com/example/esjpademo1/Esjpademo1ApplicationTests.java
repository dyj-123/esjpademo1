package com.example.esjpademo1;

import com.example.esjpademo1.person.dao.EsPersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class Esjpademo1ApplicationTests {

    @Test
    void contextLoads() {
    }


    @Resource
    private EsPersonRepository esPersonRepository;

//    @Test
//    @DisplayName("插入或更新单个文档")/
//    void createPerson() {
//        esPersonRepository.save(EsPerson.builder()
//
//                .name("老六")
//                .age(33)
//                .build());
//    }

}
