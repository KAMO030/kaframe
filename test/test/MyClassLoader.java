package test;


import basedao_test.dao.CinfoDao;
import com.kamo.context.annotation.Autowired;
import com.kamo.context.annotation.Component;


@Component
public class MyClassLoader implements B {
    @Autowired
    private CinfoDao cinfoDao;
//    @Autowired
//    private B d;
//    @Autowired
//    private JDBCTemplate jdbcTemplate;
//    @Reference("1")
//    private TestService testService;

    //    public void test() {
//        System.out.println(d);
//    }

    @Override
    public void t() {
        cinfoDao.count(null);
//        System.out.println(serviceImp);
//        serviceImp.test("11");
    }
}
