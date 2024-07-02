package com.zy.foundation.lang;

import com.zy.foundation.lang.security.RSATools;
import org.junit.Test;

import java.util.Map;

public class RSAToolsTest {
    @Test
    public void fn01() {
        Map<String, Object> initKey = RSATools.initKey(8192);

        // 公钥加密
        String publicKeyEncoded = RSATools.getPublicKey(initKey);
        System.out.println("公钥为: " + publicKeyEncoded);
        String encrypt = RSATools.encryptByPublicKey("monkey", publicKeyEncoded);
        System.out.println("加密后的密文为: " + encrypt);

        // 私钥解密
        String privateKeyEncoded = RSATools.getPrivateKey(initKey);
        System.out.println("私钥为: " + privateKeyEncoded);
        String decrypt = RSATools.decryptByPrivateKey(encrypt, privateKeyEncoded);
        System.out.println("解密后的明文为: " + decrypt);
    }
}
