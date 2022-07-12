package kamo.context.annotation;

import kamo.context.BeanDefinitionRegistry;
import kamo.context.BeanFactory;
import kamo.context.Resolve;

import java.lang.reflect.Method;

public class ConfigurationClassResolve implements Resolve {

    private BeanDefinitionRegistry registry;
    private Class configBeanClass;

    public ConfigurationClassResolve(BeanDefinitionRegistry registry,Class configBeanClass) {
        this.configBeanClass = configBeanClass;
        this.registry = registry;
    }
    @Override
    public void parse(){
        if (needScan()) {
            scan();
        }
        if (needLoadMethods()) {
            loadMethods();
        }
    }

    private void scan() {
        String[] basePackages =((ComponentScan) configBeanClass.getAnnotation(ComponentScan.class)).value();
        new ClassPathBeanDefinitionScanner(registry).scan(basePackages);
    }

    protected boolean needScan() {
        return configBeanClass.isAnnotationPresent(ComponentScan.class);
    }

    protected void loadMethods() {
        Method[] methods = configBeanClass.getMethods();
        Object configBean;
        configBean = ((BeanFactory)registry).getBean(configBeanClass);
        for (Method method : methods) {
            if (method.isAnnotationPresent(Bean.class)) {
                String name = method.getAnnotation(Bean.class).name();
                name = name.equals("") ? method.getName() : name;
                MethodBeanDefinition methodBeanDefinition = new MethodBeanDefinition(configBean,method);
                new AutowiredPropertyResolve(methodBeanDefinition).parse();
                registry.registerBeanDefinition(name,methodBeanDefinition);
            }
        }
    }

    protected boolean needLoadMethods() {
        boolean flag = true;
        return flag;
    }
}
