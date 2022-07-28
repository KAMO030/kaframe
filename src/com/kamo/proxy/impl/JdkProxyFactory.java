package com.kamo.proxy.impl;

import com.kamo.proxy.Advisor;
import com.kamo.proxy.AdvisorRegister;
import com.kamo.proxy.ProxyFactory;
import com.kamo.proxy.TargetSource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class JdkProxyFactory implements InvocationHandler , ProxyFactory {


    private TargetSource targetSource;
    private List<Advisor> advisorList ;
    public JdkProxyFactory(TargetSource targetSource) {
        setTargetSource(targetSource);
    }

    public JdkProxyFactory(Object target) {
        setTargetSource(new SingletonTargetSource(target));
    }

   public Object getProxy(){
      return Proxy.newProxyInstance(getClass().getClassLoader(),targetSource.getTargetClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Object target = getTargetSource().getTarget();
        if (method .getName().equals("toString")&&args==null) {
            return targetSource.getTargetClass().getName()+"$Proxy@"+Integer.toHexString(target.hashCode());
        }
        JdkMethodInvocation invocation = new JdkMethodInvocation(target, method, args,advisorList);
        return invocation.invoke(invocation);
    }
    public TargetSource getTargetSource() {
        return targetSource;
    }
    @Override
    public void addAdvisor(Advisor advisor) {
        advisorList.add(advisor);
    }

    public void setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
        setAdvisor(AdvisorRegister.getAdvisors(targetSource.getTargetClass()));
    }

    public void setAdvisor(List<Advisor> advisor) {
        this.advisorList = advisor;
    }
}
