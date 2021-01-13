package com.tarang.dpq2.model;

public class KeyValueModel {
    String key = "";
    String value = "";

    public KeyValueModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValueModel(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
