package proxy;

import kamo.proxy.annotation.After;
import kamo.proxy.annotation.Before;
import kamo.proxy.annotation.AspectJ;

@AspectJ
public class AspectJTest {
    @After("test.ServiceImp.test()")
    public void after(){
        System.out.println("test:after");
    }
    @After("test.ServiceImp.test()")
    public void afte1r(){
        System.out.println("test:after1");
    }
    @After("test.ServiceImp.test1()")
    public void aft2r(){
        System.out.println("test1:after2");
    }
    @Before("test.ServiceImp.test1()")
    public void before(){
        System.out.println("test1:before1");
    }
    @Before("test.ServiceImp.test()")
    public void before1(){
        System.out.println("test1:before");
    }
}
