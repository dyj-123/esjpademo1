package com.example.esjpademo1.person.entity;


import com.example.esjpademo1.annotation.OriginData;
import com.example.esjpademo1.annotation.SensitiveField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@OriginData
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "newperson",createIndex = true)
@Setting(shards = 3, replicas = 1,refreshInterval="1ms")
public class EsPerson implements Serializable {

    /** 居委 */

    private String juwei;
    /** 姓名 */
    @SensitiveField(value = "MD5",isKeyword = true)
    @Field(type = FieldType.Keyword)
    private String name;
    /** 性别 */
    @Field(type = FieldType.Keyword)
    private String sex;
    /** 年龄 */
    @Field(type = FieldType.Integer)
    private Integer age;
    /** 身份证号码 */
    @Id
    @Field(type = FieldType.Keyword)
    @SensitiveField(value = "MD5")
    private String card;
    /** 户籍地址 */

    @SensitiveField(value = "MD5")
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String hujiaddress;

    @SensitiveField(value = "MD5")
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String jzaddress;

    private String addressremark;
    /** 手机号码 */
    @SensitiveField(value = "MD5")
    @Field(type = FieldType.Keyword)
    private String telephone;
    /** 楼栋 */
    @Field(type = FieldType.Keyword)
    private String loudong;
    /** 长护险等级 */
    private Integer changhuxianlevel;

    @Field(type = FieldType.Integer)
    private Integer personId;
    /** 备注 */

    private String moreinfo;

    @Field(type = FieldType.Keyword)
    private String city;

    @Field(type = FieldType.Keyword)
    private String hao;

    @Field(type = FieldType.Keyword)
    private String jzcity;

    @Field(type = FieldType.Keyword)
    private String jzhao;

    @Field(type = FieldType.Keyword)
    private String jzprovince;

    @Field(type = FieldType.Keyword)
    private String jzregion;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String jzroad;

    @SensitiveField(value = "MD5")
    @Field(type = FieldType.Keyword)
    private String jzroom;

    @Field(type = FieldType.Keyword)
    private String jzstreet;

    @Field(type = FieldType.Keyword)
    private String province;

    @Field(type = FieldType.Keyword)
    private String region;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String road;

    @SensitiveField(value = "MD5")
    @Field(type = FieldType.Keyword)
    private String room;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String nong;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String jznong;

    @Field(type = FieldType.Keyword)
    private String street;

    @Field(type = FieldType.Keyword)
    private String rhfenli;

    private List<Long[]> labelIds;

    private List<Long> inttIds;

    @CreatedDate
    @Field(type = FieldType.Date,format={DateFormat.basic_date, DateFormat.year_month_day})
    private Instant ingest_timestamp;

    @SensitiveField(value = "RSA")
    @Field(type = FieldType.Keyword)
    private String originData;

    private String _class;

}