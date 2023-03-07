package com.example.esjpademo1.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cxy
 */
@Data
public class ResultBean<T> {
    private int status;
    private String msg;
    private int count;
    private T data;

    public ResultBean() {
        msg = "success";
    }

    public static ResultBean success() {
        ResultBean resultBean = new ResultBean();

        resultBean.setStatus(200);
        resultBean.setMsg(null);
        return resultBean;
    }

    public static ResultBean success(List data, int total) {
        ResultBean resultBean = new ResultBean();

        resultBean.setStatus(200);
        resultBean.setMsg(null);
        resultBean.setCount(total);
        resultBean.setData(data);
        return resultBean;
    }

    public static ResultBean success(String msg) {
        ResultBean resultBean = new ResultBean();

        resultBean.setStatus(200);
        resultBean.setMsg(msg);
        resultBean.setCount(0);

        return resultBean;
    }

    public static ResultBean success(Object data) {
        ResultBean resultBean = new ResultBean();

        resultBean.setStatus(200);
        resultBean.setData(data);
        resultBean.setCount(0);
        return resultBean;
    }

    public static ResultBean success(Object data, int count) {
        ResultBean resultBean = new ResultBean();

        resultBean.setStatus(200);
        resultBean.setData(data);
        resultBean.setCount(count);
        return resultBean;
    }

    public static ResultBean error(String message) {
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(500);
        resultBean.setData(new ArrayList<>());
        resultBean.setMsg(message);
        resultBean.setCount(0);

        return resultBean;
    }


    public static ResultBean error(Object data) {
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(500);
        resultBean.setCount(0);
        resultBean.setData(data);

        return resultBean;
    }

    public static ResultBean error(Integer code, String msg) {
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(code);
        resultBean.setMsg(msg);
        return resultBean;
    }

    public static ResultBean warn() {
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(300);
        resultBean.setCount(0);
        return resultBean;
    }
    public static ResultBean warn(String message) {
        ResultBean resultBean = new ResultBean();
        resultBean.setStatus(300);
        resultBean.setData(new ArrayList<>());
        resultBean.setMsg(message);
        resultBean.setCount(0);

        return resultBean;
    }



    public static ResultBean warn(Object data,String msg) {
        ResultBean resultBean = new ResultBean();

        resultBean.setStatus(300);
        resultBean.setData(data);
        resultBean.setMsg(msg);
        resultBean.setCount(0);
        return resultBean;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
