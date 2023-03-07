package com.example.esjpademo1.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.alibaba.fastjson.JSON;

import com.example.esjpademo1.annotation.OriginData;
import com.example.esjpademo1.annotation.SensitiveField;
import com.example.esjpademo1.config.RsaProperties;
import com.example.esjpademo1.person.dao.EsKeywordRepository;
import com.example.esjpademo1.person.entity.EsKeyword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Component
@Slf4j
public class EncryptUtil {

//    private static ElasticsearchClient client;
    private static Object origin;
    private static EsKeywordRepository esKeywordRepository;
    public EncryptUtil(EsKeywordRepository esKeywordRepository) {
        EncryptUtil.esKeywordRepository = esKeywordRepository;
    }

    public static void handleItem(Object item) throws Exception {

        Field[] fields = item.getClass().getDeclaredFields();
        if(null!=AnnotationUtils.findAnnotation(item.getClass(), OriginData.class)){
            System.out.println(item.getClass().getDeclaredConstructor());
            origin  = item.getClass().getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(item,origin);
        }
        // 遍历所有字段
        for (Field field : fields) {
            // 若该字段被SecurityField注解,则进行解密/加密
            Class<?> fieldType = field.getType();
            if (fieldType == String.class) { //String类型
                if (null != AnnotationUtils.findAnnotation(field, SensitiveField.class)) {
                         // 设置private类型允许访问
                        field.setAccessible(Boolean.TRUE);
                        handleField(item, field);
                        field.setAccessible(Boolean.FALSE);
                }
            } else if (List.class.isAssignableFrom(field.getType())) {//如果该字段类型可以转换成list
                Type genericType = field.getGenericType();
                // 是否参数化类型
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) genericType;
                    Class genericClazz = (Class) pt.getActualTypeArguments()[0];
                    if (genericClazz == String.class && null != AnnotationUtils.findAnnotation(field, SensitiveField.class)) {
                        field.setAccessible(Boolean.TRUE);
                        List list = (List) field.get(item);
                        if(list != null && !list.isEmpty()){
                            for (int i=0; i < list.size(); i++) {
                                //加密
                                String value = field.getAnnotation(SensitiveField.class).value();
                                if(value.equals("MD5")){
                                    field.set(item,MD5Util.MD5Method((String) field.get(item)));
                                }else if(value.equals("RSA")){
                                    list.set(i, RsaUtils.publicKeyEncrypt((String) list.get(i), RsaProperties.publicKey));
                                }
                            }
                        }
                        field.setAccessible(Boolean.FALSE);
                    } else {
                        Field[] subFields = genericClazz.getDeclaredFields();
                        for (Field subField : subFields) {
                            if (subField.getType() == String.class && null != AnnotationUtils.findAnnotation(subField, SensitiveField.class)) {
                                field.setAccessible(Boolean.TRUE);
                                List list = (List) field.get(item);
                                if(list != null && !list.isEmpty()) {
                                    for (Object subObj : list) {
                                        subField.setAccessible(Boolean.TRUE);
                                        handleField(subObj, subField);
                                        subField.setAccessible(Boolean.FALSE);
                                    }
                                    field.setAccessible(Boolean.FALSE);
                                }
                            }
                        }
                    }
                }
            } else if(fieldType.getName().startsWith("com.example.esdemo")){//如果是自己的类
                Field[] subFields = fieldType.getDeclaredFields();
                for (Field subField : subFields) {
                    if (subField.getType() == String.class && null != AnnotationUtils.findAnnotation(subField, SensitiveField.class)) {
                        field.setAccessible(Boolean.TRUE);
                        Object obj = field.get(item);
                        subField.setAccessible(Boolean.TRUE);
                        handleField(obj, subField);
                        subField.setAccessible(Boolean.FALSE);
                        field.setAccessible(Boolean.FALSE);
                    }
                }
            }
            // T responseData由于泛型擦除，通过反射无法直接获取到实际类型
            else if(fieldType == Object.class){
                field.setAccessible(Boolean.TRUE);
                handleItem(field.get(item));
                field.setAccessible(Boolean.FALSE);
            }
        }
    }

    private static void handleField(Object item, Field field) throws Exception {
        if(null == item){
            return;
        }
        //如果是关键字字段存到es的keywords索引中
        if(null!=AnnotationUtils.findAnnotation(item.getClass(), OriginData.class)&&field.get(item)!=null&&field.getAnnotation(SensitiveField.class).isKeyword()){
            EsKeyword esKeyword = new EsKeyword();
            esKeyword.setName(field.getName());
            esKeyword.setId(MD5Util.MD5Method((String) field.get(item)));
            esKeyword.setValue((String) field.get(item));
            esKeywordRepository.save(esKeyword);
        }
        String value = field.getAnnotation(SensitiveField.class).value();
        if(value.equals("MD5")){
                field.set(item,MD5Util.MD5Method((String) field.get(item)));
        }else if(value.equals("RSA")){
                String json = JSON.toJSON(origin).toString();
                field.set(item, RsaUtils.publicKeyEncrypt(json,RsaProperties.publicKey));
        }

    }



}
