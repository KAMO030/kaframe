package test;


import kamo.context.annotation.Autowired;
import kamo.context.annotation.Component;
import proxy.Service;

@Component
public class MyClassLoader implements B {
//    @Autowired
//    private B d;
//    @Autowired
//    private JDBCTemplate jdbcTemplate;
//    @Reference("1")
//    private TestService testService;

    //    public void test() {
//        System.out.println(d);
//    }
    @Autowired
    private Service serviceImp;

    @Override
    public void t() {
        System.out.println(serviceImp);
        serviceImp.test("11");
    }
}
