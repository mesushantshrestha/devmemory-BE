package com.shrestha.devmemory_BE.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Type {
    TASK,
    REMEMBER,
    SNIPPET,
    IDEA ;

    @JsonCreator
    public static Type from(String value) {
        return Type.valueOf(value.trim().toUpperCase());
    }
}
