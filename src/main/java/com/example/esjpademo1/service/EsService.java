package com.example.esjpademo1.service;

import com.example.esjpademo1.person.entity.EsKeyword;
import com.example.esjpademo1.person.entity.EsPerson;
import com.example.esjpademo1.person.entity.EsPersonQuery;

import java.io.IOException;

public interface EsService {
    void addPerson(EsPerson esPerson);

    Object keywordSearch(String value);

    Object searchPerson(EsPersonQuery esPersonQuery, Integer curPage, Integer pageSize) throws IOException;

    int inport() throws Exception;

    Object decrypt(String str) throws Exception;

    void delPerson(String[] cards);

    void editPerson(EsPerson esPerson);
}
