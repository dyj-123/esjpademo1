package com.example.esjpademo1.person.dao;

import com.example.esjpademo1.person.entity.EsPerson;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface EsPersonRepository extends ElasticsearchRepository<EsPerson, String> {

    Optional<EsPerson> findByCard(String card);

    void deleteByCardIn(List<String> cards);
}
