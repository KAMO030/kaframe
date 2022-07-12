package kamo.proxy.impl;

import kamo.proxy.Advisor;
import kamo.proxy.AdvisorRegister;
import kamo.proxy.ProxyFactory;
import kamo.proxy.TargetSource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class JdkProxyFactory implements InvocationHandler , ProxyFactory {


    private TargetSource targetSource;
    private List<Advisor> advisorList = AdvisorRegister.getAdvisors();
    public JdkProxyFactory(TargetSource targetSource) {
        this.targetSource = targetSource;
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
            return targetSource.getTargetClass().getName()+"$$Proxy@"+target.hashCode();
        }
        if (method .getName().equals("getClass")&&args==null){
            return proxy.getClass();
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
    }

    public void setAdvisor(List<Advisor> advisor) {
        this.advisorList = advisor;
    }
}
