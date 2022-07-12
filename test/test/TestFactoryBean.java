package test;

import kamo.context.FactoryBean;

//@Component
public class TestFactoryBean implements FactoryBean<RemoteTest> {
    @Override
    public RemoteTest getObject() {
        return new RemoteTest();
    }

    @Override
    public Class getObjectType() {
        return RemoteTest.class;
    }
}
