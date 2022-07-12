package kamo.context.factory;

public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
