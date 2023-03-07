package com.example.esjpademo1.person.dao;

import com.example.esjpademo1.person.entity.EsPerson;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsPersonRepository extends ElasticsearchRepository<EsPerson, Long> {
}
