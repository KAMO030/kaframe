package test;

import com.kamo.context.annotation.Autowired;
import com.kamo.context.factory.InitializingBean;

//@Component
public class D implements B,InitializingBean {
    @Autowired
    private B myClassLoader;
//    @Reference("2")
//    private TestService testService;
    @Override
    public void t() {
//        System.out.println(myClassLoader);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        System.out.println(testService);
    }
}
