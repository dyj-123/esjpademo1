package com.example.esjpademo1.controller;


import com.example.esjpademo1.annotation.SensitiveMethod;
import com.example.esjpademo1.person.entity.EsKeyword;
import com.example.esjpademo1.person.entity.EsPerson;
import com.example.esjpademo1.person.entity.EsPersonQuery;
import com.example.esjpademo1.service.EsService;
import com.example.esjpademo1.utils.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Api(tags="测试操作")
@RequestMapping("person")
public class EsController {

    private final EsService esService;

    @ApiOperation("添加一个人")
    @PostMapping
    @SensitiveMethod
    public ResultBean addPerson(@RequestBody  EsPerson esPerson) throws Exception {
        esService.addPerson(esPerson);
        return ResultBean.success();
    }

    @ApiOperation("根据身份证删除一个人")
    @DeleteMapping
    public ResultBean delPerson(@RequestBody String[] cards){
        esService.delPerson(cards);
        return ResultBean.success();
    }

    @ApiOperation("修改一个人")
    @PutMapping
    @SensitiveMethod
    public ResultBean editPerson(@RequestBody  EsPerson esPerson) throws Exception {
        esService.editPerson(esPerson);
        return ResultBean.success();
    }

    @ApiOperation("数据库导入")
    @GetMapping("/inport")
    public ResultBean inportAll() throws Exception {

        int num = esService.inport();
        return ResultBean.success("成功导入"+num);
    }


    @ApiOperation("自动填充ES")
    @GetMapping("/keywordSearch")
    public ResultBean keywordSearch(String value) throws IOException {
        return ResultBean.success(esService.keywordSearch(value));
    }

    @ApiOperation("模糊查询分页demo")
    @GetMapping("/searchPerson")
    @SensitiveMethod
    public ResultBean searchPerson(EsPersonQuery esPersonQuery, Integer curPage, Integer pageSize) throws IOException {
        return ResultBean.success(esService.searchPerson(esPersonQuery,curPage,pageSize));
    }

    @ApiOperation("解密测试")
    @GetMapping("/decrypt")
    public ResultBean decrypt(String str) throws Exception {
        return ResultBean.success(esService.decrypt(str));
    }

}
