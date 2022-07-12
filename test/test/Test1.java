package test;

import kamo.context.BeanDefinition;
import kamo.context.factory.BeanInstanceProcessor;

public class Test1 implements BeanInstanceProcessor {
    public Test1(){

    }
    public Test1(String[] name){
        System.out.println(1);
    }
    @Override
    public Object instanceBefore(String beanName, BeanDefinition beanDefinition) {
        System.out.println(beanName);
        return null;
    }

    @Override
    public void instanceAfter(String beanName, BeanDefinition beanDefinition, Object bean) {

    }
}
