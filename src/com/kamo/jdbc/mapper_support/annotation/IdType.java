package com.kamo.jdbc.mapper_support.annotation;

import com.kamo.jdbc.mapper_support.IdTypeAssignAuto;
import com.kamo.jdbc.mapper_support.IdTypeAssignInput;
import com.kamo.jdbc.mapper_support.IdTypeAssignUUID;
import com.kamo.jdbc.mapper_support.IdTypeStrategy;

public enum IdType {

    AUTO(new IdTypeAssignAuto()),  //自增
//    NONE(1), //未设置主键
    INPUT(new IdTypeAssignInput()), //手动输入
//    ASSIGN_ID(3),  //默认全局唯一ID
    ASSIGN_UUID(new IdTypeAssignUUID()); //全局唯一的 uuid
    private final IdTypeStrategy strategy;
    IdType(IdTypeStrategy strategy) {
        this.strategy = strategy;
    }

    public IdTypeStrategy getStrategy() {
        return strategy;
    }
}
