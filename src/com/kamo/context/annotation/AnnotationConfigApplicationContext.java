package com.kamo.context.annotation;

import com.kamo.context.AbstractApplicationContext;
import com.kamo.context.condition.impl.AnnotationConditionMatcher;
import com.kamo.context.listener.annotation.ApplicationListenerPostProcessor;

public class AnnotationConfigApplicationContext extends AbstractApplicationContext implements AnnotationConfigRegistry {
    private  AnnotatedBeanDefinitionReader reader;

    private  ClassPathBeanDefinitionScanner scanner;

    public AnnotationConfigApplicationContext() {
        init();
    }
    public AnnotationConfigApplicationContext(ClassLoader classLoader,Class<?>... componentClasses) {
        super(classLoader);
        init();
        register(componentClasses);
        refresh();
    }
    public AnnotationConfigApplicationContext(ClassLoader classLoader, String... basePackages) {
        this();
        scan(basePackages);
        refresh();
    }
    public AnnotationConfigApplicationContext(Class<?>... componentClasses) {
        // 构造DefaultListableBeanFactory、AnnotatedBeanDefinitionReader、ClassPathBeanDefinitionScanner
        this();
        register(componentClasses);
        refresh();
    }
    public AnnotationConfigApplicationContext(String... basePackages) {
        this();
        scan(basePackages);
        refresh();
    }
    private void init(){
        reader = new AnnotatedBeanDefinitionReader(this);
        scanner = new ClassPathBeanDefinitionScanner(new AnnotationConditionMatcher(this),this);
    }
    @Override
    public void registerBeanFactoryPostProcessors() {
        super.registerBeanFactoryPostProcessors();
        register(
                ConfigurationRegistryPostProcessor.class,
                AutowiredArgsInstanceProcessor.class,
                ApplicationListenerPostProcessor.class,
                AnnotationConditionMatcher.class
        );
    }



    @Override
    public void register(Class... componentClasses) {
        this.reader.register(componentClasses);
    }



    @Override
    public void scan(String... basePackages) {
        this.scanner.scan(basePackages);
    }
}
