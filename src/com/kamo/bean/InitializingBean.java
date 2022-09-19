package com.kamo.bean;

public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
