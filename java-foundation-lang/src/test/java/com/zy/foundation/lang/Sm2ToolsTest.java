package com.zy.foundation.lang;

import com.zy.foundation.lang.security.Sm2Tools;
import org.apache.commons.codec.binary.Base64;

import java.security.KeyPair;

public class Sm2ToolsTest {

    /**
     * main 方法测试
     **/
    public static void main(String[] args) {
        KeyPair keyPair = Sm2Tools.generateSm2KeyPair();
        String privateKey = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());
        String publicKey = Base64.encodeBase64String(keyPair.getPublic().getEncoded());

        System.out.println("公钥：" + publicKey);
        System.out.println("私钥：" + privateKey);

        String data = "张三李四";
        String encrypt = Sm2Tools.encrypt(publicKey, data);
        String decrypt = Sm2Tools.decrypt(privateKey, encrypt);

        System.out.println("加密后数据：" + encrypt);
        System.out.println("解密后数据：" + decrypt);
    }

}
