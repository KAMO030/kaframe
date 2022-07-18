package com.kamo.cloud;

public interface Protocol {

    void export(URL url);
    Invoker refer(URL url);
}
