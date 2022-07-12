package kamo.context.annotation;
import kamo.context.AbstractApplicationContext;

public class AnnotationConfigApplicationContext extends AbstractApplicationContext implements AnnotationConfigRegistry {
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

        singletonBeans.put("annotationConfigApplicationContext",this);
        register(this.getClass());
        registerConfiguration(new ConfigurationClassPostProcessor());
        registerConfiguration(new PropertySetProcessor(this));
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
