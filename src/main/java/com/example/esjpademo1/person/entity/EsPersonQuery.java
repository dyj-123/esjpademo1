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
}
