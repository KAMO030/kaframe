package com.kamo.proxy.impl;

import com.kamo.context.factory.BeanFactoryPostProcessor;
import com.kamo.context.factory.ConfigurableListableBeanFactory;

public class ProxyConfig implements BeanFactoryPostProcessor {
    private AspectScanner scanner;
    private String[] path;

    public ProxyConfig(String[] path) {
        this.path = path;
        scanner = new AspectScanner();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

        scanner.scan(path);
        beanFactory.registerConfiguration(new ProxyBeanPostProcessor());
    }
}
