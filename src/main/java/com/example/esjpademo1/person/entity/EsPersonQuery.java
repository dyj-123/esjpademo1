package com.example.esjpademo1.person.entity;


import com.example.esjpademo1.annotation.SensitiveField;
import lombok.Data;

import java.util.List;

@Data
public class EsPersonQuery {
    private String juwei;
    /** 姓名 */
    @SensitiveField(value = "MD5")
    private String name;
    /** 性别 */
    private String sex;
    /** 年龄 */
    private List<Integer> age;

    /** 需要加密 */
    @SensitiveField(value = "MD5")
    private String card;

    /** 需要加密 */
    @SensitiveField(value = "MD5")
    private String telephone;

    /** BETWEEN长护险等级 */
    private List<Integer> changhuxianlevel;

    private String road;

    private String nong;

    private String hao;

    /** 需要加密 */
    @SensitiveField(value = "MD5")
    private String room;

    private String jzroad;

    private String jznong;

    private String jzhao;

    private List<Integer> id;

    /** 需要加密 */
    @SensitiveField(value = "MD5")
    private String jzroom;

    private String rhfenli; //人户分离

    private List<String> labelIds;
}
