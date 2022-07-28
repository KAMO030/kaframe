package proxy_test.aspect;

import com.kamo.proxy.annotation.After;
import com.kamo.proxy.annotation.Aspect;
import com.kamo.proxy.annotation.Before;

@Aspect
public class AspectJTest {
    @After("proxy_test.service.imp.CinfoServiceImp.service()")
    public void after(){
        System.out.println("service:after");
    }
    @After("proxy_test.service.imp.CinfoServiceImp.service()")
    public void afte1r(){
        System.out.println("service:after1");
    }
    @After("proxy_test.service.imp.CinfoServiceImp.service1()")
    public void aft2r(){
        System.out.println("service1:after2");
    }
    @Before("proxy_test.service.imp.CinfoServiceImp.service1()")
    public void before(){
        System.out.println("service1:before1");
    }
    @Before("proxy_test.service.imp.CinfoServiceImp.service()")
    public void before1(){
        System.out.println("service:before");
    }
//    @Before("proxy_test.Config.test()")
//    public void before2(){
//        System.out.println("service:before55");
//    }
}
