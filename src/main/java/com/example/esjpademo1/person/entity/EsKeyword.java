package com.example.esjpademo1.person.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;

@Data
@Document(indexName = "keywordsnew",createIndex = true)
@Setting(shards = 3, replicas = 1,refreshInterval="1ms")
public class EsKeyword {
    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String value;
    @Field(type = FieldType.Keyword)
    private String name;
    @Field(type = FieldType.Date,format={DateFormat.basic_date, DateFormat.year_month_day})
    private Date ingest_timestamp;
}
