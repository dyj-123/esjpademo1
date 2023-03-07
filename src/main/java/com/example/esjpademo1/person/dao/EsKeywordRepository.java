package com.example.esjpademo1.person.dao;


import com.example.esjpademo1.person.entity.EsKeyword;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EsKeywordRepository extends ElasticsearchRepository<EsKeyword, Long> {
    List<EsKeyword> findByValueLike(String value);

    @Query("{\"match\": {\"value\": {\"query\": \"?0\"}}}")
    List<EsKeyword> findByValue(String value);
}
