package com.kamo.jdbc.mapper_upport.annotation;

public enum NameType {


    SAME(0),SNAKE_CASE(1);
    private final int type;

    NameType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
