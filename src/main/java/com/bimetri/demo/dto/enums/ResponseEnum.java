package com.bimetri.demo.dto.enums;

public enum ResponseEnum {
    COURSE("course"), STUDENT("student");

    private final String name;

    ResponseEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
