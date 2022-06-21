package com.zy.foundation.lang.security.sm2;

public class SM2KeyPair {
    private final byte[] publicKeyX;
    private final byte[] publicKeyY;
    private final byte[] privateKey;

    public SM2KeyPair(byte[] publicKeyX, byte[] publicKeyY, byte[] privateKey) {
        this.publicKeyX = publicKeyX;
        this.publicKeyY = publicKeyY;
        this.privateKey = privateKey;
    }

    public byte[] getPublicKeyX() {
        return publicKeyX;
    }

    public byte[] getPublicKeyY() {
        return publicKeyY;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }
}
