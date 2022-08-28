package com.kamo.cloud;

public interface Protocol {

    void export(URL url);

    void export(URL url, Object instance);

    Invoker refer(URL url);
}
