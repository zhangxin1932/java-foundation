package com.zy.foundation.lang;

import com.zy.foundation.lang.security.Sm4Tools;

public class Sm4ToolsTest {

    public static void main(String[] args) throws Exception {
        t3();
    }

    private static void t3() throws Exception{
        // String key = "D91CEB11EE62219CD91CEB11EE62219C";
        String key = Sm4Tools.generateHexKeyOrHexIv();
        System.out.println(key);
        String raw = "张三hello张三hello张三hello张三hello张三hello张三hello";
        String encrypt = Sm4Tools.encryptEcb(Sm4Tools.Algorithm.ECB_ZeroBytePadding.getName(), key, raw);
        System.out.println(encrypt);
        String decrypt = Sm4Tools.decryptEcb(Sm4Tools.Algorithm.ECB_ZeroBytePadding.getName(), key, encrypt);
        System.out.println(decrypt);
        boolean checked = Sm4Tools.verifyEcb(Sm4Tools.Algorithm.ECB_ZeroBytePadding.getName(), key, encrypt, decrypt);
        System.out.println(checked);

        System.out.println("cbc -------------------> ");
        String hexIv = Sm4Tools.generateHexKeyOrHexIv();
        String cbcencrypt = Sm4Tools.encryptCbc(Sm4Tools.Algorithm.CBC_ZeroBytePadding.getName(), key, hexIv, raw);
        System.out.println(encrypt);
        String cbcdecrypt = Sm4Tools.decryptCbc(Sm4Tools.Algorithm.CBC_ZeroBytePadding.getName(), key, hexIv, cbcencrypt);
        System.out.println(decrypt);
        /*boolean cbcchecked = Sm4Tools.ver(Sm4Tools.Algorithm.CBC_PKCS5Padding.getName(), key, hexIv, cbcencrypt, cbcdecrypt);
        System.out.println(cbcchecked);*/

    }

}
