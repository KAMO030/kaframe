package com.kamo.jdbc.mapper_upport;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;


public class IdTypeAssignUUID implements IdTypeStrategy{

    @Override
    public String assembly(List args, Object entity, Field idField) {
        //自动随机一个唯一主键
        args.add(UUID.randomUUID().toString().replace("-", ""));
        return "?";
    }
}
