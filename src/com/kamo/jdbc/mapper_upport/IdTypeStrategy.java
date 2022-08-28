package com.kamo.jdbc.mapper_upport;

import java.lang.reflect.Field;
import java.util.List;

public interface IdTypeStrategy {
    String assembly(List args, Object entity, Field idField) ;
}
