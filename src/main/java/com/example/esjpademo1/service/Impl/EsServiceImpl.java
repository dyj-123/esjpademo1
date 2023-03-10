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
import com.example.esjpademo1.utils.MD5Util;
import com.example.esjpademo1.utils.RsaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class EsServiceImpl implements EsService {
    private final String PERSONS="newperson";
    private final ElasticsearchClient client;

    private final EsPersonRepository esPersonRepository;
    private final EsKeywordRepository esKeywordRepository;
    private final PersonRepository personRepository;


    public EsServiceImpl(ElasticsearchClient elasticsearchClien, EsPersonRepository esPersonRepository, EsKeywordRepository esKeywordRepository, PersonRepository personRepository) {
        this.client = elasticsearchClien;
//        this.elasticsearchOperations = elasticsearchOperations;
        this.esPersonRepository = esPersonRepository;
        this.esKeywordRepository = esKeywordRepository;
        this.personRepository = personRepository;
    }
    @Override
    public void addPerson(EsPerson esPerson) {
        //新增之前判断是否已经存在
        //先将card加密
//        String card = MD5Util.MD5Method(esPerson.getCard());
        Optional<EsPerson> person = esPersonRepository.findByCard(esPerson.getCard());
        if (person.isPresent()){
            //存在返回错误信息
            System.out.println("身份证号码已存在,请勿重复添加");
            return;
        }
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
            co.elastic.clients.elasticsearch._types.query_dsl.Query byName = MatchQuery.of(m->m.field("name").query(esPersonQuery.getName()))._toQuery();
            queries.add(byName);
        }
        if (esPersonQuery.getCard()!=null){
            Query byCard = MatchQuery.of(m -> m.field("card").query(esPersonQuery.getCard()))._toQuery();
            queries.add(byCard);
        }
        if (esPersonQuery.getTelephone()!=null){
            Query byTelephone = MatchQuery.of(m -> m.field("telephone").query(esPersonQuery.getTelephone()))._toQuery();
            queries.add(byTelephone);
        }
        if(esPersonQuery.getJuwei()!=null){
            co.elastic.clients.elasticsearch._types.query_dsl.Query byJuwei = MatchQuery.of(m->m.field("juwei").query(esPersonQuery.getJuwei()))._toQuery();
            queries.add(byJuwei);
        }
        if(esPersonQuery.getSex()!=null){
            co.elastic.clients.elasticsearch._types.query_dsl.Query bySex = MatchQuery.of(m->m.field("sex").query(esPersonQuery.getSex()))._toQuery();
            queries.add(bySex);
        }
        if (esPersonQuery.getRoad()!=null){
            Query byRoad = MatchQuery.of(m -> m.field("road").query(esPersonQuery.getRoad()))._toQuery();
            queries.add(byRoad);
        }
        if (esPersonQuery.getNong()!=null){
            Query byNong = MatchQuery.of(m -> m.field("nong").query(esPersonQuery.getNong()))._toQuery();
            queries.add(byNong);
        }
        if (esPersonQuery.getHao()!=null){
            Query byHao = MatchQuery.of(m -> m.field("hao").query(esPersonQuery.getHao()))._toQuery();
            queries.add(byHao);
        }
        if (esPersonQuery.getRoom()!=null){
            //加密
            String room = MD5Util.MD5Method(esPersonQuery.getRoom());
            Query byRoom = MatchQuery.of(m -> m.field("room").query(room))._toQuery();
            queries.add(byRoom);
        }
        if (esPersonQuery.getJzroad()!=null){
            Query byJzroad = MatchQuery.of(m -> m.field("jzroad").query(esPersonQuery.getJzroad()))._toQuery();
            queries.add(byJzroad);
        }
        if (esPersonQuery.getJznong()!=null){
            Query byJznong = MatchQuery.of(m -> m.field("jznong").query(esPersonQuery.getJznong()))._toQuery();
            queries.add(byJznong);
        }
        if (esPersonQuery.getJzhao()!=null){
            Query byJzhao = MatchQuery.of(m -> m.field("jzhao").query(esPersonQuery.getJzhao()))._toQuery();
            queries.add(byJzhao);
        }
        if (esPersonQuery.getJzroom()!=null){
            //加密
            String jzroom = MD5Util.MD5Method(esPersonQuery.getJzroom());
            Query byJzroom = MatchQuery.of(m -> m.field("jzroom").query(jzroom))._toQuery();
            queries.add(byJzroom);
        }
        if (esPersonQuery.getRhfenli()!=null){
            Query byRhfenli = MatchQuery.of(m -> m.field("rhfenli").query(esPersonQuery.getRhfenli()))._toQuery();
            queries.add(byRhfenli);
        }
        if(esPersonQuery.getAge()!=null){
            List<Integer> agerange = esPersonQuery.getAge();
            co.elastic.clients.elasticsearch._types.query_dsl.Query byAge = Query.of(q->q.range(r->r.field("age").from(String.valueOf(agerange.get(0))).to(String.valueOf(agerange.get(1)))));
            queries.add(byAge);
        }
        if(esPersonQuery.getChanghuxianlevel()!=null){
            List<Integer> range = esPersonQuery.getChanghuxianlevel();
            Query byChanghuxian = Query.of(q->q.range(r->r.field("changhuxianlevel").from(String.valueOf(range.get(0))).to(String.valueOf(range.get(1)))));
            queries.add(byChanghuxian);
        }
        if (esPersonQuery.getLabelIds()!=null){
            List<String> labelIds = esPersonQuery.getLabelIds();
            List<List<Long>> querylist = new ArrayList<>();
            for (String item : labelIds) {
                String[] split = item.split("\\+");
                Long labelId = Long.parseLong(split[0]);
                Long level = Long.parseLong(split[1]);
                List<Long> data = new ArrayList<>();
                data.add(labelId);
                data.add(level);
                querylist.add(data);
            }
            //TODO 添加标签的查询条件，前端传过来的数据类型是["2+1","1+-1"]
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
//            esPersonRepository.save(esPerson);
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
        List<Person> persons = personRepository.findAll();
        int len = persons.size();
        int num = len/1000;
        List<Person> addperson = new ArrayList<>();
        for(int i = 0;i<persons.size();i++){

            if(addperson.size()==num){
                int num1 =bulkinport(addperson);
                log.info("本次导入{}",num1);
                addperson.clear();
            }else{
                addperson.add(persons.get(i));

            }
        }
        return 0;
    }

    @Override
    public Object decrypt(String str) throws Exception {
        String res  = RsaUtils.privateKeyDecrypt(str,RsaProperties.privateKey);
        return JSONObject.parse(res);
    }

    @Override
    public void delPerson(String[] cards) {
        //先将身份证号加密
        List<String> MDcards = new ArrayList<>();
        for (String card : cards) {
            MDcards.add(MD5Util.MD5Method(card));
        }
        esPersonRepository.deleteByCardIn(MDcards);
    }

    @Override
    public void editPerson(EsPerson esPerson) {
        esPersonRepository.save(esPerson);
    }
}
