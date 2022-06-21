package com.zy.foundation.lang.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

/**
 * https://gitee.com/superch/crypto-gmsm/blob/master/pom.xml
 * https://gitee.com/shenshuxin01/sm4_cbc_utils/blob/util-1-base64/src/main/java/com/sm4_test/Test.java#
 * https://www.jianshu.com/p/ae607d2a1e6a
 * https://www.bookstack.cn/read/hutool/3409b7f1fbc6e74e.md
 */
public class Sm4Tools {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @AllArgsConstructor
    @Getter
    public enum Algorithm {
        /**
         * PKCS5Padding  NoPadding 补位规则，PKCS5Padding缺位补0，NoPadding不补
         */
        ECB_PKCS5Padding("SM4/ECB/PKCS5Padding"),
        ECB_PKCS7Padding("SM4/ECB/PKCS7Padding"),
        ECB_ZeroBytePadding("SM4/ECB/ZeroBytePadding"),

        // CBC
        CBC_PKCS5Padding("SM4/CBC/PKCS5Padding"),
        CBC_PKCS7Padding("SM4/CBC/PKCS7Padding"),
        CBC_ZeroBytePadding("SM4/CBC/ZeroBytePadding"),
        ;
        private final String name;
    }

    private static final String ENCODING = "UTF-8";
    private static final String ALGORITHM_NAME = "SM4";
    private static final int KEY_SIZE = 128;

    public static String generateHexKeyOrHexIv() throws Exception {
        return ByteUtils.toHexString(generateKeyOrIv());
    }

    public static byte[] generateKeyOrIv() throws Exception {
        KeyGenerator instance = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        instance.init(KEY_SIZE, new SecureRandom());
        return instance.generateKey().getEncoded();
    }

    /**
     * ECB加密模式，无向量
     *
     * @param algorithmName 算法名称
     * @param mode          模式
     * @param key           key
     * @return 结果
     */
    private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }

    private static Cipher generateCbcCipher(String algorithmName, int mode, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
            Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(mode, sm4Key, ivParameterSpec);
            return cipher;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * CBC P5填充加密
     * 优点：安全性高
     * 缺点：不利于并行计算，误差传递，需要初始化向量iv
     */
    public static String encryptCbc(String algorithm, String hexKey, String hexIv, String rawText) {
        try {
            Cipher cipher = generateCbcCipher(algorithm, Cipher.ENCRYPT_MODE,
                    ByteUtils.fromHexString(hexKey), ByteUtils.fromHexString(hexIv));
            byte[] encryptBytes = cipher.doFinal(rawText.getBytes(ENCODING));
            return ByteUtils.toHexString(encryptBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * CBC P5填充解密
     * @param hexKey 密钥
     * @param hexIv 偏移量，CBC每轮迭代会和上轮结果进行异或操作，由于首轮没有可进行异或的结果，
     *           所以需要设置偏移量，一般用密钥做偏移量
     * @param encryptText 加密数据
     * @return 解密结果
     */
    public static String decryptCbc(String algorithm, String hexKey, String hexIv, String encryptText) {
        try {
            Cipher cipher = generateCbcCipher(algorithm, Cipher.DECRYPT_MODE,
                    ByteUtils.fromHexString(hexKey), ByteUtils.fromHexString(hexIv));
            byte[] decryptBytes = cipher.doFinal(ByteUtils.fromHexString(encryptText));
            return new String(decryptBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String encryptEcb(String algorithmName, String hexKey, String plainText) {
        try {
            String cipherText = "";
            // 16进制字符串-->byte[]
            byte[] keyData = ByteUtils.fromHexString(hexKey);
            // String-->byte[]
            //当加密数据为16进制字符串时使用这行
            byte[] srcData = plainText.getBytes(ENCODING);
            // 加密后的数组
            byte[] cipherArray = encryptEcbPadding(algorithmName, keyData, srcData);
            // byte[]-->hexString
            cipherText = ByteUtils.toHexString(cipherArray);
            return cipherText;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptEcbPadding(String algorithmName, byte[] key, byte[] data) {
        try {
            //声称Ecb暗号,通过第二个参数判断加密还是解密
            Cipher cipher = generateEcbCipher(algorithmName, Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptEcb(String algorithmName, String hexKey, String encryptText) {
        try {
            // 用于接收解密后的字符串
            String decryptStr = "";
            // hexString-->byte[]
            byte[] keyData = ByteUtils.fromHexString(hexKey);
            // hexString-->byte[]
            byte[] cipherData = ByteUtils.fromHexString(encryptText);
            // 解密
            byte[] srcData = decryptEcbPadding(algorithmName, keyData, cipherData);
            // byte[]-->String
            decryptStr = new String(srcData, ENCODING);
            return decryptStr;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decryptEcbPadding(String algorithmName, byte[] key, byte[] encryptText) {
        try {
            //生成Ecb暗号,通过第二个参数判断加密还是解密
            Cipher cipher = generateEcbCipher(algorithmName, Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(encryptText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyEcb(String algorithmName, String hexKey, String encryptText, String plainText) {
        try {
            // 用于接收校验结果
            boolean flag = false;
            // hexString-->byte[]
            byte[] keyData = ByteUtils.fromHexString(hexKey);
            // 将16进制字符串转换成数组
            byte[] cipherData = ByteUtils.fromHexString(encryptText);
            // 解密
            byte[] decryptData = decryptEcbPadding(algorithmName, keyData, cipherData);
            // 将原字符串转换成byte[]
            byte[] srcData = plainText.getBytes(ENCODING);
            // 判断2个数组是否一致
            flag = Arrays.equals(decryptData, srcData);
            return flag;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
