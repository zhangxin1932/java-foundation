package com.zy.foundation.lang.security.sm4;

/**
 * @Description: SM4对称加解密算法中的模式和padding方式枚举类
 */
public enum SM4ModeAndPaddingEnum {
    /**
     * ECB 模式
     */
    SM4_ECB_NoPadding("SM4/ECB/NoPadding"),
    SM4_ECB_PKCS5Padding("SM4/ECB/PKCS5Padding"),
    SM4_ECB_PKCS7Padding("SM4/ECB/PKCS7Padding"),
    SM4_ECB_ZeroBytePadding("SM4/ECB/ZeroBytePadding"),

    /**
     * CBC 模式
     */
    SM4_CBC_NoPadding("SM4/CBC/NoPadding"),
    SM4_CBC_PKCS5Padding("SM4/CBC/PKCS5Padding"),
    SM4_CBC_PKCS7Padding("SM4/CBC/PKCS7Padding"),
    SM4_CBC_ZeroBytePadding("SM4/CBC/ZeroBytePadding"),

    ;

    private final String name;

    SM4ModeAndPaddingEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
