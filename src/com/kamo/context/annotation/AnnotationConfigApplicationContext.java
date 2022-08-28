package com.kamo.context.annotation;

import com.kamo.context.AbstractApplicationContext1;

public class AnnotationConfigApplicationContext extends AbstractApplicationContext1 implements AnnotationConfigRegistry {
    private final AnnotatedBeanDefinitionReader reader;

    private final ClassPathBeanDefinitionScanner scanner;

    public AnnotationConfigApplicationContext() {
        reader = new AnnotatedBeanDefinitionReader(this);
        scanner = new ClassPathBeanDefinitionScanner(this);
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
    @Override
    public void registerBeanFactoryPostProcessors() {
        super.registerBeanFactoryPostProcessors();
        register(

                ConfigurationRegistryPostProcessor.class,
                AutowiredArgsInstanceProcessor.class
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
