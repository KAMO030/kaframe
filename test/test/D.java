package test;

import com.kamo.bean.annotation.Autowired;
import com.kamo.bean.InitializingBean;

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
