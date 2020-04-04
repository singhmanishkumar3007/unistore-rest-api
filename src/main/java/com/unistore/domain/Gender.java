package com.unistore.domain;

public enum Gender {

    MALE("M"), FEMALE("F");

    private String value;

    private Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
