/*
package com.zy.foundation.lang.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import sun.misc.BASE64Decoder;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
public class Sm2Tools {

    private static final String ALGORITHM_NAME = "EC";

    */
/**
     * SM2加密算法
     *
     * @param publicKeyStr 公钥
     * @param data         明文数据
     * @return
     *//*

    public static String encrypt(String publicKeyStr, String data) {
        Security.addProvider(new BouncyCastleProvider());
        PublicKey publicKey;
        try {
            byte[] keyBytes;
            keyBytes = (new BASE64Decoder()).decodeBuffer(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("failed to transform publicKeyStr.", e);
        }
        ECPublicKeyParameters ecPublicKeyParameters = null;
        if (publicKey instanceof BCECPublicKey) {
            BCECPublicKey bcecPublicKey = (BCECPublicKey) publicKey;
            ECParameterSpec ecParameterSpec = bcecPublicKey.getParameters();
            ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
                    ecParameterSpec.getG(), ecParameterSpec.getN());
            ecPublicKeyParameters = new ECPublicKeyParameters(bcecPublicKey.getQ(), ecDomainParameters);
        }
        SM2Engine sm2Engine = new SM2Engine();
        sm2Engine.init(true, new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom()));
        byte[] arrayOfBytes;
        try {
            byte[] in = data.getBytes(StandardCharsets.UTF_8);
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
            return Base64.encodeBase64String(arrayOfBytes);
        } catch (Exception e) {
            throw new RuntimeException("failed to encrypt data.", e);
        }
    }

    */
/**
     * SM2解密算法
     *
     * @param privateKeyStr 私钥
     * @param cipherData    密文数据
     * @return
     *//*

    public static String decrypt(String privateKeyStr, String cipherData) {
        PrivateKey privateKey;
        byte[] keyBytes;
        try {
            keyBytes = (new BASE64Decoder()).decodeBuffer(privateKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("failed to transform privateKeyStr.", e);
        }

        BCECPrivateKey bcecPrivateKey = (BCECPrivateKey) privateKey;
        ECParameterSpec ecParameterSpec = bcecPrivateKey.getParameters();
        ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
                ecParameterSpec.getG(), ecParameterSpec.getN());
        ECPrivateKeyParameters ecPrivateKeyParameters = new ECPrivateKeyParameters(bcecPrivateKey.getD(),
                ecDomainParameters);
        SM2Engine sm2Engine = new SM2Engine();
        sm2Engine.init(false, ecPrivateKeyParameters);

        byte[] arrayOfBytes;
        try {
            byte[] in = Base64.decodeBase64(cipherData);
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
            return new String(arrayOfBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("failed to decrypt data.", e);
        }
    }

    */
/**
     * SM2算法生成密钥对
     *
     * @return 密钥对信息
     *//*

    public static KeyPair generateSm2KeyPair() {
        try {
            final ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
            // 获取一个椭圆曲线类型的密钥对生成器
            final KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM_NAME, new BouncyCastleProvider());
            SecureRandom random = new SecureRandom();
            // 使用SM2的算法区域初始化密钥生成器
            kpg.initialize(sm2Spec, random);
            // 获取密钥对
            return kpg.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("failed to generateSm2KeyPair.");
        }
    }

}
*/
