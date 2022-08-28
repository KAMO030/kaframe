package com.kamo.jdbc.mapper_upport.annotation;

public enum IdType {

    AUTO(0),  //自增
    NONE(1), //未设置主键
    INPUT(2), //手动输入
    ASSIGN_ID(3),  //默认全局唯一ID
    ASSIGN_UUID(4); //全局唯一的 uuid
    private final int type;
    IdType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
