package basedao_test;

import basedao_test.pojo.Cinfo;
import basedao_test.service.CinfoService;
import com.kamo.context.ApplicationContext;
import com.kamo.context.annotation.AbstractScanner;
import com.kamo.context.annotation.AnnotationConfigApplicationContext;
import com.kamo.jdbc.BeanPropertyRowMapper;
import com.kamo.jdbc.JDBCTemplate;
import com.kamo.jdbc.basedao.BaseDaoImp;
import com.kamo.jdbc.basedao.SQL;
import com.kamo.util.BeanUtil;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

//basedao和BFactoryBean演示demo
public class Mian {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
//        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
//
//        CinfoService cinfoServiceImp = (CinfoService) context.getBean("cinfoServiceImp");
//        Cinfo cinfo = new Cinfo();
//        cinfo.setcName("可");
//        System.out.println(cinfoServiceImp.service(cinfo));
//        BaseDaoFactory baseDaoFactory = BaseDaoFactoryBuild.build("db.properties");
//        CinfoDao baseDao = baseDaoFactory.getBaseDao(CinfoDao.class);
//        Cinfo cinfo = new Cinfo();
//        cinfo.setcName("可乐");
//        System.out.println(baseDao.u(cinfo));
//        System.out.println(baseDao.selectCinfoVoList(cinfo));
//        Long l = new Long("111");
//        Integer.valueOf(Math.toIntExact(l));
//        System.out.println(Integer.class.getDeclaredField("TYPE").get(null));
//        System.out.println(List.class.isAssignableFrom(ArrayList.class));
//        Properties props = new Properties();
//        System.out.println("可乐");
//        try {
//            props.load(Mian.class.getClassLoader().getResourceAsStream("db.properties"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        IzumiDataSource dataSource = IzumiDataSourceFactory.createDataSource(props);
//        Cinfo cinfo = new Cinfo();
//        cinfo.setcName("可乐");
//        CinfoDao dao = getMapping(CinfoDao.class,dataSource);
//        System.out.println(dao.selectCinfoVoList(cinfo));
    }

    private static <T> T getMapping(Class<T> daoClass, DataSource dataSource) {
        return (T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{daoClass}, new InvocationHandler() {
            Class<T> entityClass = (Class) ((ParameterizedType) daoClass.getGenericInterfaces()[0]).getActualTypeArguments()[0];
            JDBCTemplate jdbcTemplate = new JDBCTemplate(dataSource);
            BaseDaoImp baseDao = new BaseDaoImp(entityClass,jdbcTemplate);

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object result = null;
                try {
                    result = method.invoke(baseDao, args);
                } catch (IllegalArgumentException e) {
                    result = invoke(method, args);
                }
                return result;
            }

            private Object invoke(Method method, Object[] args) {
                String sql = method.getAnnotation(SQL.class).value();
                List params = new ArrayList();
                int argIndex = 0;
                while (sql.matches("(.*)\\$\\{(.*)\\}(.*)")) {
                    argIndex = argIndex > args.length-1?args.length-1:argIndex++;
                    int stratIndex = sql.indexOf("${") + 2;
                    int endIndex = sql.indexOf("}", stratIndex);
                    String $auto = sql.substring(stratIndex, endIndex);
                    $auto = BeanUtil.autoStitchingSql(args[argIndex++], $auto, params);
                    sql = sql.substring(0, stratIndex-2)+$auto+ sql.substring(endIndex+1);
                }
                Class<?> returnType = method.getReturnType();
                if (returnType.isAssignableFrom(List.class)) {
                    returnType = (Class) ((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[0];
                }
                return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(returnType), params.toArray());
            }
        });
    }
}
