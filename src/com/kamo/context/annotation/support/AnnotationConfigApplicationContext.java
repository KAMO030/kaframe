package com.kamo.context.annotation.support;

import com.kamo.context.GenericApplicationContext;
import com.kamo.context.annotation.AnnotationConfigRegistry;
import com.kamo.context.condition.impl.AnnotationConditionMatcher;
import com.kamo.context.listener.impl.ApplicationListenerInstanceProcessor;

public class AnnotationConfigApplicationContext extends GenericApplicationContext implements AnnotationConfigRegistry {
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
        scanner = new ClassPathBeanDefinitionScanner(this);
    }
    @Override
    public void registerBeanFactoryPostProcessorBeanDefinitions() {
        super.registerBeanFactoryPostProcessorBeanDefinitions();
        register(
                ConfigurationRegistryPostProcessor.class,
                AutowiredArgsInstanceProcessor.class,
                ApplicationListenerInstanceProcessor.class,
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
