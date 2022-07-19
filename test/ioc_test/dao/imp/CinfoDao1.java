package ioc_test.dao.imp;

import basedao_test.service.pojo.Cinfo;
import ioc_test.dao.CinfoDao;

public class CinfoDao1 implements CinfoDao {
    @Override
    public int update(Cinfo cinfo) {
        System.out.println("我是CinfoDao1");
        return 0;
    }
}
