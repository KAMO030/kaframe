package com.kamo.transaction.support;


import com.kamo.context.BeanDefinition;
import com.kamo.context.BeanFactory;
import com.kamo.context.annotation.Autowired;
import com.kamo.context.exception.BeansException;
import com.kamo.context.factory.BeanInstanceProcessor;
import com.kamo.context.factory.BeanPostProcessor;
import com.kamo.proxy.Advisor;
import com.kamo.proxy.AdvisorRegister;
import com.kamo.transaction.TransactionManager;
import com.kamo.transaction.annotation.Transactional;


public class TransactionManagerBeanPostProcessor implements BeanInstanceProcessor {
    @Autowired
    private BeanFactory factory;

    @Override
    public Object instanceBefore(String beanName, BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        TransactionAdvisorBuilder builder = new TransactionAdvisorBuilder(beanClass);
        if (builder.isEnableTransaction()) {
            String managerName = null;
            if (beanClass.isAnnotationPresent(Transactional.class)) {
                managerName = beanClass.getAnnotation(Transactional.class).managerName();
            }
            final String finalManagerName = managerName;
            Advisor advisor = managerName != null ?
                    builder.buildAdvisor(() -> factory.getBean(finalManagerName, TransactionManager.class)) :
                    builder.buildAdvisor(() -> factory.getBean(TransactionManager.class));
            AdvisorRegister.registerAdvisor(advisor);
        }
        return BeanInstanceProcessor.super.instanceBefore(beanName, beanDefinition);
    }
}
