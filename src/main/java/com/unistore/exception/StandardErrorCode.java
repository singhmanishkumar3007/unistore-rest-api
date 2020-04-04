package com.unistore.exception;

public enum StandardErrorCode {
    SC404("Not Found"), SC500("Internal Server Error"), SC400("Bad Message");

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private StandardErrorCode(String value) {
        this.value = value;
    }

}
