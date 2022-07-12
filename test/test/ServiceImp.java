package test;

import kamo.context.annotation.Autowired;
import kamo.context.annotation.Component;
import proxy.Service;

@Component
public class ServiceImp implements Service {
    @Autowired
    B myClassLoader;
    @Autowired
    Service service;

    @Override
    public String test(String v) {
        service.test1("1");
        System.out.println(service);
        System.out.println(myClassLoader);
        return "hello";
    }

    @Override
    public String test1(String v) {
        System.out.println(v);
        return "hello11";
    }
}
