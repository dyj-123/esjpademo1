package com.example.esjpademo1.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
@Component
public class MD5Util {
    @Value("${md5.salt}")
    private static String salt;
    public static String MD5Method(String str){

        if(str==null||str.equals("")){
            return str;
        }
        // 基于spring框架中的DigestUtils工具类进行密码加密
        String hashedPwd1 = DigestUtils.md5DigestAsHex((str + salt).getBytes());
        return hashedPwd1;
    }
    public static void main(String[] args) {
        String pwd = "123456";
        String salt = "vme50";
        // 基于spring框架中的DigestUtils工具类进行密码加密
        String hashedPwd1 = DigestUtils.md5DigestAsHex((pwd + salt).getBytes());
        System.out.println(hashedPwd1);
    }
}
