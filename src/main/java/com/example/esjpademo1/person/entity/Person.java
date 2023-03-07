package com.example.esjpademo1.person.entity;/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


import lombok.Data;

import io.swagger.annotations.ApiModelProperty;

import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;



import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @website https://eladmin.vip
 * @description /
 * @author dyj
 * @date 2022-10-17
 **/
@Entity

@NoArgsConstructor
@Data
@Accessors(chain = true)
@Table(name="sys_person",uniqueConstraints = {@UniqueConstraint(columnNames="card")})
public class Person implements Serializable {
    private static final long serialVersionUID = 1019466745376831818L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`person_id`")
    @ApiModelProperty(value = "id")
    private Long id;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @ApiModelProperty(value = "老人标签")
//    @JoinTable(name = "sys_persons_labels",
//            joinColumns = {@JoinColumn(name = "person_id",referencedColumnName = "person_id")},
//            inverseJoinColumns = {@JoinColumn(name = "label_id",referencedColumnName = "label_id")})
//    private Set<Labels> labels;

    @Column(name = "`juwei`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "居委")
    private String juwei;

    @Column(name = "`name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "姓名")
    private String name;

    @Column(name = "`sex`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "性别")
    private String sex;

    @Column(name = "`age`",nullable = false)
    @NotNull
    @ApiModelProperty(value = "年龄")
    private Integer age;

    @Column(name = "`card`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "身份证号码")
    private String card;

    @Column(name = "`hujiaddress`")
    @ApiModelProperty(value = "户籍地址")
    private String hujiaddress;

    @Column(name = "`telephone`")
    @ApiModelProperty(value = "手机号码")
    private String telephone;

    @Column(name = "`addressremark`")
    @ApiModelProperty(value = "地址别名")
    private String addressremark;

    @Column(name = "`jzaddress`")
    @ApiModelProperty(value = "居住地址")
    private String jzaddress;

    @Column(name = "`province`")
    @ApiModelProperty(value = "省")
    private String province;

    @Column(name = "`city`")

    @ApiModelProperty(value = "市")
    private String city;

    @Column(name = "`region`")

    @ApiModelProperty(value = "区")
    private String region;

    @Column(name = "`street`")
    @ApiModelProperty(value = "街道")
    private String street;

    @Column(name = "`road`")
    @ApiModelProperty(value = "路/村")
    private String road;
    @Column(name = "`nong`")
    @ApiModelProperty(value = "号")
    private String nong;


    @Column(name = "`hao`")
    @ApiModelProperty(value = "号")
    private String hao;

    @Column(name = "`room`")
    @ApiModelProperty(value = "室")

    private String room;
    @Column(name = "`jzprovince`")
    @ApiModelProperty(value = "省")
    private String jzprovince;

    @Column(name = "`jzcity`")
    @ApiModelProperty(value = "市")
    private String jzcity;

    @Column(name = "`jzregion`")
    @ApiModelProperty(value = "区")
    private String jzregion;

    @Column(name = "`jzstreet`")
    @ApiModelProperty(value = "街道")
    private String jzstreet;

    @Column(name = "`jzroad`")
    @ApiModelProperty(value = "路/村")
    private String jzroad;

    @Column(name = "`jznong`")
    @ApiModelProperty(value = "弄")
    private String jznong;

    @Column(name = "`jzhao`")
    @ApiModelProperty(value = "号")
    private String jzhao;

    @Column(name = "`jzroom`")
    @ApiModelProperty(value = "室")
    private String jzroom;

    @Column(name = "`rhfenli`")
    @ApiModelProperty(value = "人户分离")
    private String rhfenli;



    @Column(name = "`changhuxianlevel`")
    @ApiModelProperty(value = "长护险等级")
    private Integer changhuxianlevel;


    @Column(name = "`moreinfo`")
    @ApiModelProperty(value = "moreinfo")
    private String moreinfo;


}
