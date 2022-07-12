package kamo.proxy.impl;

import kamo.proxy.Advisor;
import kamo.proxy.AdvisorRegister;
import kamo.proxy.MethodInvocation;
import kamo.proxy.Pointcut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class JdkMethodInvocation implements MethodInvocation {
    private  Object target;
    private Method method;
    private Object[] args;
    private int index;
    private List<Advisor> advisorList;
    public int getIndex() {
        return index++;
    }


    public JdkMethodInvocation(Object target, Method method, Object[] args, List<Advisor> advisorList) {
        this.target = target;
        this.method = method;
        this.args = args;
        this.advisorList = advisorList;
    }

   public Object proceed() throws Throwable {
        return this.invoke(this);
   }

    public Object invoke(JdkMethodInvocation invocation) throws Throwable {
        int index = invocation.getIndex();
        if (index== AdvisorRegister.advisorSize()) {
            return invocation.invoke();
        }
        Advisor advisor = AdvisorRegister.getAdvisor(index);
        Pointcut pointcut = advisor.getPointcut();
        return pointcut.matches(invocation.getMethod(), target.getClass())
                ?advisor.getAdvice().invoke(invocation)
                :invoke(invocation);
    }
    @Override
    public Object getThis() {
        return null;
    }

    public Object invoke() throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target,args);
   }
    public Object getTarget() {
        return target;
    }
    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public Object getArguments() {
        return args;
    }

}

