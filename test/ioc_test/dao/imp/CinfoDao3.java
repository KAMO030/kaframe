package ioc_test.dao.imp;

import basedao_test.pojo.Cinfo;
import ioc_test.dao.CinfoDao;

public class CinfoDao3 implements CinfoDao {
    @Override
    public int update(Cinfo cinfo) {
        System.out.println("我是CinfoDao3");
        return 0;
    }
}
