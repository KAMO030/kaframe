package kamo.proxy;

public  class AbstractAdvisor implements Advisor  {
    private Advice advice;
    private Pointcut pointcut ;




    @Override
    public boolean classFilter(Class<?> targetClass) {
        return pointcut.classFilter(targetClass);
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }


    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }



    public void setPointcut(Pointcut pointcut) {
        this.pointcut = pointcut;
    }




}
