package proxy;

import kamo.datasource.IzumiDataSourceFactory;
import kamo.jdbc.BeanPropertyRowMapper;
import kamo.jdbc.JDBCTemplate;
import kamo.proxy.impl.JdkProxyFactory;
import test.ServiceImp;
import test.entity.User;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Properties;

public class ProxyFactoryTest {
    static DataSource dataSource;
    static {
        Properties props = new Properties();
        try {
            props.load(ProxyFactoryTest.class.getClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        dataSource = IzumiDataSourceFactory.createDataSource(props);
    }
    public static void main(String[] args) throws IOException {

        new Test(AspectJTest.class);
        ServiceImp serviceImp = new ServiceImp();
        JdkProxyFactory factory = new JdkProxyFactory(serviceImp);
        Service proxy = (Service) factory.getProxy();
        proxy.test("77");
//       ull;
//        serviceImp.test("额温枪");
//     UserMapper mapper = getProxy(UserMapper.class);
//        List<User> query = mapper.query();
//        System.out.println(query);
//        System.out.println(proxy.test("1"));

//        System.out.println(proxy.test1("2"));
}



    public static <T> T getProxy(Class<T> type) {
        return (T) Proxy.newProxyInstance(ProxyFactoryTest.class.getClassLoader(), new Class[]{UserMapper.class}, new InvocationHandler() {
            private JDBCTemplate jdbcTemplate = new JDBCTemplate(dataSource);

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                List query = null;
                if (method.isAnnotationPresent(Sql.class)) {
                    Sql sql = method.getAnnotation(Sql.class);
                    String value = sql.value();
                    BeanPropertyRowMapper userBeanPropertyRowMapper = new BeanPropertyRowMapper<>(User.class);
                    query = jdbcTemplate.query(value, userBeanPropertyRowMapper);
                }
                return query;
            }
        });
    }
}
