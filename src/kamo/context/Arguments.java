package kamo.context;

import kamo.context.annotation.Arg;

import java.beans.Introspector;
import java.lang.reflect.Parameter;

public class Arguments {
    private String name;
    private Class type;
    private Object value;

    public Arguments(Parameter parameter) {
        setType(parameter.getType());
        setName(parameter.isAnnotationPresent(Arg.class)
                ?parameter.getAnnotation(Arg.class).value()
                : Introspector.decapitalize(parameter.getType().getSimpleName()));
    }

    public Arguments() {

    }

    public Arguments(String name, Class type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }
}
