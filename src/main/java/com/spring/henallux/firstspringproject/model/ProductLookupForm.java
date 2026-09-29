package com.spring.henallux.firstspringproject.model;

public class MagicKeyForm {
    public MagicKeyForm() {
    }

    public String getMagicKey() {
        return magicKey;
    }

    public MagicKeyForm(String magicKey) {
        this.magicKey = magicKey;
    }

    public void setMagicKey(String magicKey) {
        this.magicKey = magicKey;
    }

    private String magicKey;
}
