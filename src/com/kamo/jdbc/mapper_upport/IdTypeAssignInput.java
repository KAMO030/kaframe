package com.kamo.jdbc.mapper_upport;

import java.lang.reflect.Field;
import java.util.List;

public class IdTypeAssignInput implements IdTypeStrategy{
    @Override
    public String assembly(List args, Object entity, Field idField) {
        idField.setAccessible(true);
        try {
            Object idValue = idField.get(entity);
            args.add(idValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return "?";
    }
}
