package kamo.context;

import kamo.context.annotation.Lazy;

import java.lang.reflect.Field;

public class Property {
    private String name;
    private Object value;
    private Field field;
    private Class type;
    private boolean isLazed;
    public Property(Field field){
        init(field);
    }



    private void init(Field field) {
        this.field = field;
        this.type=field.getType();
        this.name = field.getName();
        this.isLazed = field.isAnnotationPresent(Lazy.class);
    }

    public Field getField() {
        return field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }
    public Class getValueType() {
        return value==null ?null : value.getClass();
    }
    public void setValue(Object value) {
        this.value = value;
    }


    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public boolean isLazed() {
        return isLazed;
    }

    public void setLazed(boolean lazed) {
        isLazed = lazed;
    }
}
