package com.kamo.context.listener;

import java.util.EventObject;

public abstract   class ApplicationEvent<T> extends EventObject {
    private static final long serialVersionUID = 7099057708183571937L;
    private final long timestamp = System.currentTimeMillis();

    public ApplicationEvent(T source) {
        super(source);
    }

    public final long getTimestamp() {
        return this.timestamp;
    }
}