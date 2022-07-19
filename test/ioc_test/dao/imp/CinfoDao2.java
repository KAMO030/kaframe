package ioc_test.dao.imp;

import basedao_test.pojo.Cinfo;
import ioc_test.dao.CinfoDao;

public class CinfoDao2 implements CinfoDao {
    @Override
    public int update(Cinfo cinfo) {
        System.out.println("我是CinfoDao2");
        return 0;
    }
}