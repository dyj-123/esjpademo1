package com.example.esjpademo1.utils;


import com.example.esjpademo1.annotation.SensitiveField;
import com.example.esjpademo1.config.RsaProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author https://www.cnblogs.com/nihaorz/p/10690643.html
 * @description Rsa 工具类，公钥私钥生成，加解密
 * @date 2020-05-18
 **/
@Component
@Slf4j
public class RsaUtils {



    private static final String PUBLIC_KEY="publicKey";
    private static final String PRIVATE_KEY="privateKey";

    public static void main(String[] args) throws Exception {
        //随机生成公钥和秘钥
        Map<String, String> keyMap = genKeyPair();
        System.out.println("随机公钥:"+keyMap.get(PUBLIC_KEY));
        System.out.println("随机私钥:"+keyMap.get(PRIVATE_KEY));
        String publicKey = keyMap.get(PUBLIC_KEY);
        String privateKey = keyMap.get(PRIVATE_KEY);
        //加密字符串
        String message = "{\"allBillList\":[{\"id\":\"264786\",\"billNo\":\"WB2021051700064\",\"billTypeId\":13,\"billTypeName\":\"定损维修\",\"vehicleNo\":\"京Q683976\",\"vehicleId\":\"532928\",\"vehicleOwner\":\"北京测试公司\",\"vehicleModelName\":\"宝马\",\"orderCar\":0,\"orderCarName\":\"短租\",\"cityId\":null,\"cityName\":\"北京\",\"deptId\":null,\"deptName\":\"北京测试\",\"nowCityId\":null,\"nowCityName\":null,\"nowDeptId\":null,\"nowDeptName\":null,\"belongCityId\":null,\"belongCityName\":\"北京\",\"belongDeptId\":null,\"belongDeptName\":\"知春路店\",\"costBelongAreaName\":\"知春路大片区\",\"costBelongCityName\":\"北京\",\"costBelongDeptName\":\"知春路店\",\"createTime\":\"2021-05-17 10:11\",\"modifyTime\":null,\"billStatusId\":109,\"billStatusName\":\"方案已通过\",\"createEmpName\":\"JD\",\"createEmpDeptName\":\"北京测试\",\"pickupType\":2,\"pickupTypeName\":\"上门\",\"isDelete\":0,\"isDeleteeName\":\"否\",\"garageId\":null}],\"totalCount\":1}";
        System.out.println("前端请求的原数据:"+message);
        String messageEn = publicKeyEncrypt(message, publicKey);
        System.out.println("前端请求的加密:" + messageEn);
        String messageDe = privateKeyDecrypt(messageEn, privateKey);
        System.out.println("后端解密出来的数据:" + messageDe);
        System.out.println("==========================================");
        //前端数据展示处理
        //私钥加密，公钥解密
        String s = privateKeyEncrypt(messageDe, privateKey);
        System.out.println("后端返回的加密数据:"+s);
        String s1 = publicKeyDecrypt(s, publicKey);
        System.out.println("前端解密出来显示的数据:"+s1);
    }

    /**
     * 功能描述:
     * 〈随机生成密钥对〉
     *
     * @return : java.util.Map<java.lang.String,java.lang.String>
     * @author gggcgba 【wechat:13031016567】
     * @date : 2021/5/17 0017 17:38
     */
    public static Map<String, String> genKeyPair() throws NoSuchAlgorithmException {
        System.out.println("开始生成公钥私钥对");
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();    // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();        // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        Map<String, String> map = new HashMap<>();
        map.put(PUBLIC_KEY, publicKeyString);
        map.put(PRIVATE_KEY, privateKeyString);
        return map;
    }


    /**
     * RSA私钥加密
     *
     * @author gggcgba 【wechat:13031016567】
     * @param str
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String privateKeyEncrypt(String str, String privateKey) throws Exception {
//        log.info("{}|RSA私钥加密前的数据|str:{}|publicKey:{}",str);
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        PrivateKey priKey = KeyFactory.getInstance("RSA").
                generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, priKey);

        //当长度过长的时候，需要分割后加密 117个字节
        byte[] resultBytes = getMaxResultEncrypt(str, cipher);

        String outStr = Base64.encodeBase64String(resultBytes);
//        log.info("{}|RSA私钥加密后的数据|outStr:{}",outStr);
        return outStr;
    }

    /**
     * RSA公钥解密
     *
     * @author gggcgba 【wechat:13031016567】
     * @param str
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String publicKeyDecrypt(String str, String publicKey) throws Exception {
//        log.info("{}|RSA公钥解密前的数据|str:{}|publicKey:{}",str);
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        PublicKey pubKey =  KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        //当长度过长的时候，需要分割后解密 128个字节
        String outStr = new String(getMaxResultDecrypt(str, cipher));
//        log.info("{}|RSA公钥解密后的数据|outStr:{}",outStr);
        return outStr;
    }





    /**
     * RSA公钥加密
     *
     *
     * @author gggcgba 【wechat:13031016567】
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String publicKeyEncrypt(String str, String publicKey) throws Exception {
//        log.info("{}|RSA公钥加密前的数据|str:{}|publicKey:{}", str);
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").
                generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);

        //当长度过长的时候，需要分割后加密 117个字节
        byte[] resultBytes = getMaxResultEncrypt(str, cipher);

        String outStr = Base64.encodeBase64String(resultBytes);
//        log.info("{}|公钥加密后的数据|outStr:{}", outStr);
        return outStr;
    }

    private static byte[] getMaxResultEncrypt(String str, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
        byte[] inputArray = str.getBytes();
        int inputLength = inputArray.length;
        log.info("{}|加密字节数|inputLength:{}", inputLength);
        // 最大加密字节数，超出最大字节数需要分组加密
        int MAX_ENCRYPT_BLOCK = 117;
        // 标识
        int offSet = 0;
        byte[] resultBytes = {};
        byte[] cache = {};
        while (inputLength - offSet > 0) {
            if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
                offSet += MAX_ENCRYPT_BLOCK;
            } else {
                cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
                offSet = inputLength;
            }
            resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
            System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
        }
        return resultBytes;
    }

    /**
     * RSA私钥解密
     *
     * @author gggcgba 【wechat:13031016567】
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String privateKeyDecrypt(String str, String privateKey) throws Exception {
        log.info("{}|RSA私钥解密前的数据|str:{}|privateKey:{}", str);
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
//        String outStr = new String(cipher.doFinal(inputByte));
        //当长度过长的时候，需要分割后解密 128个字节
        String outStr = new String(getMaxResultDecrypt(str, cipher));
        log.info("{}|RSA私钥解密后的数据|outStr:{}", outStr);
        return outStr;
    }

    private static byte[] getMaxResultDecrypt(String str, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        byte[] inputArray = Base64.decodeBase64(str.getBytes("UTF-8"));
        int inputLength = inputArray.length;
        log.info("{}|解密字节数|inputLength:{}", inputLength);
        // 最大解密字节数，超出最大字节数需要分组加密
        int MAX_ENCRYPT_BLOCK = 128;
        // 标识
        int offSet = 0;
        byte[] resultBytes = {};
        byte[] cache = {};
        while (inputLength - offSet > 0) {
            if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
                offSet += MAX_ENCRYPT_BLOCK;
            } else {
                cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
                offSet = inputLength;
            }
            resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
            System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
        }
        return resultBytes;
    }
}