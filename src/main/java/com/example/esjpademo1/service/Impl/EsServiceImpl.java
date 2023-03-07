package com.example.esjpademo1.service.Impl;



import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.esjpademo1.config.RsaProperties;
import com.example.esjpademo1.person.dao.EsKeywordRepository;
import com.example.esjpademo1.person.dao.EsPersonRepository;
import com.example.esjpademo1.person.dao.PersonRepository;
import com.example.esjpademo1.person.entity.EsPerson;
import com.example.esjpademo1.person.entity.EsPersonQuery;
import com.example.esjpademo1.person.entity.Person;
import com.example.esjpademo1.service.EsService;
import com.example.esjpademo1.utils.EncryptUtil;
import com.example.esjpademo1.utils.RsaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class EsServiceImpl implements EsService {
    private final String PERSONS="newperson";
    private final ElasticsearchClient client;

    private final EsPersonRepository esPersonRepository;
    private final EsKeywordRepository esKeywordRepository;
    private final PersonRepository personRepository;
    static ThreadLocal<List<Person>> addperson = new ThreadLocal<>();


    public EsServiceImpl(ElasticsearchClient elasticsearchClien, EsPersonRepository esPersonRepository, EsKeywordRepository esKeywordRepository, PersonRepository personRepository) {
        this.client = elasticsearchClien;
//        this.elasticsearchOperations = elasticsearchOperations;
        this.esPersonRepository = esPersonRepository;
        this.esKeywordRepository = esKeywordRepository;
        this.personRepository = personRepository;
    }
    @Override
    public void addPerson(EsPerson esPerson) {
        esPersonRepository.save(esPerson);
    }

    @Override
    public Object keywordSearch(String value) {

        return esKeywordRepository.findByValue(value);
    }

    @Override
    public Object searchPerson(EsPersonQuery esPersonQuery, Integer curPage, Integer pageSize) throws IOException {
        List<co.elastic.clients.elasticsearch._types.query_dsl.Query> queries = new ArrayList<>();

        if(esPersonQuery.getName()!=null){
            System.out.println(esPersonQuery.getName());
            co.elastic.clients.elasticsearch._types.query_dsl.Query byName = MatchQuery.of(m->m.field("name").query(esPersonQuery.getName()))._toQuery();
            queries.add(byName);
        }
        if(esPersonQuery.getJuwei()!=null){
            co.elastic.clients.elasticsearch._types.query_dsl.Query byJuwei = MatchQuery.of(m->m.field("juwei").query(esPersonQuery.getJuwei()))._toQuery();
            queries.add(byJuwei);
        }
        if(esPersonQuery.getSex()!=null){
            co.elastic.clients.elasticsearch._types.query_dsl.Query bySex = MatchQuery.of(m->m.field("sex").query(esPersonQuery.getSex()))._toQuery();
            queries.add(bySex);
        }
        if(esPersonQuery.getAge()!=null){
            List<Integer> agerange = esPersonQuery.getAge();
            co.elastic.clients.elasticsearch._types.query_dsl.Query byAge = Query.of(q->q.range(r->r.field("age").from(String.valueOf(agerange.get(0))).to(String.valueOf(agerange.get(1)))));
            queries.add(byAge);
        }
        SearchResponse<EsPerson> response = client.search(s->s
                        .index(PERSONS)
                        .query(q->q.bool(b->b.must(queries)))
                        .sort(st->st.field(f->f.field("ingest_timestamp").order(SortOrder.Asc)))
                ,EsPerson.class);
        TotalHits total = response.hits().total();
        List<Hit<EsPerson>> hits = response.hits().hits();
//        System.out.println(total.value());
        JSONObject res = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if(total.value()==0){
            res.put("total",total.value());
            res.put("data",jsonArray);
            return res;
        }
        String last = hits.get(hits.size()-1).sort().get(0);

        for(int i =2;i<=curPage;i++){
            String tmplast  = last;
            response = client.search(s->s
                            .index(PERSONS)
                            .size(pageSize)
                            .query(q->q.bool(b->b.must(queries)))
                            .sort(st->st.field(f->f.field("id").order(SortOrder.Asc)))
                            .searchAfter(tmplast)
                    ,EsPerson.class);
            hits = response.hits().hits();
            last = hits.get(hits.size()-1).sort().get(0);
        }

        for(Hit<EsPerson> hit:hits){
            EsPerson esPerson = hit.source();
            jsonArray.add(esPerson);
        }
        res.put("total",total.value());
        res.put("data",jsonArray);
        return res;
    }

    public int bulkinport(List<Person> persons) throws Exception {
        BulkRequest.Builder br = new BulkRequest.Builder();

        for (Person person : persons) {
            EsPerson esPerson = new EsPerson();
            BeanUtils.copyProperties(person,esPerson);
            EncryptUtil.handleItem(esPerson);
            br.operations(op -> op
                    .index(idx -> idx
                            .index(PERSONS)
                            .id(String.valueOf(esPerson.getCard()))
                            .document(esPerson)
                    )
            ).timeout(t->t.time("10s"));
        }
        BulkResponse result = null;
        try {
            result = client.bulk(br.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Log errors, if any
        if (result.errors()) {
            log.error("Bulk had errors");
            for (BulkResponseItem item : result.items()) {
                if (item.error() != null) {
                    log.error(item.error().reason());
                }
            }
        }
        return result.items().size();
    }

    @Override
    public int inport() throws Exception {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        List<Person> persons = personRepository.findAll();
        int len = persons.size();
        int num = len/100;
        for(int i = 0;i<persons.size();i++){
            int finalI = i;
            threadPool.execute(()->{
                if(addperson.get()==null){
                    addperson.set(new ArrayList<>());
                }
                if(addperson.get().size()==num){
                    int num1 = 0;
                    try {
                        num1 = bulkinport(addperson.get());
                        log.info("{}导入{}",Thread.currentThread().getName(),num1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        addperson.remove();
                    }
                }else{
//                    log.info("{}线程添加第{}个人到list",Thread.currentThread().getName(),finalI);
                    addperson.get().add(persons.get(finalI));
                }
            });

        }

        return 0;
    }

    @Override
    public Object decrypt(String str) throws Exception {
        String res  = RsaUtils.privateKeyDecrypt(str,RsaProperties.privateKey);
        return JSONObject.parse(res);
    }
}
