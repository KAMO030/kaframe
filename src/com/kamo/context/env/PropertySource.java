package com.kamo.context.env;

public abstract class PropertySource<T> {
    protected String name;
   protected T source;

    public String getName() {
        return name;
    }

    public PropertySource<T> setName(String name) {
        this.name = name;
        return this;
    }

    public T getSource() {
        return source;
    }

    public PropertySource<T> setSource(T source) {
        this.source = source;
        return this;
    }
}
