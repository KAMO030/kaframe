package com.kamo.jdbc.mapper_upport;

import java.lang.reflect.Field;
import java.util.List;

public class IdTypeAssignAuto implements IdTypeStrategy{
    @Override
    public String assembly(List args, Object entity, Field idField) {
        return null;
    }
}
