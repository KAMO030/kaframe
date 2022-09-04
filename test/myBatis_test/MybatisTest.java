package myBatis_test;

import com.kamo.idal.SqlSessionFactoryBuilder;
import myBatis_test.mapper.CinfoMapper;

public class MybatisTest {
    public static void main(String[] args) {
        CinfoMapper mapper = new SqlSessionFactoryBuilder()
                .build("mybatis.xml")
                .openSession()
                .getMapper(CinfoMapper.class);
        System.out.println(mapper.findAllCinfoVOByList());

        Class clazz = Class.class;
    }
}
