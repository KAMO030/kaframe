package ioc_test.dao.imp;

import basedao_test.pojo.Cinfo;
import com.kamo.bean.annotation.Autowired;
import com.kamo.bean.annotation.Component;

import ioc_test.dao.CinfoDao;
@Component
//@Lazy
public class CinfoDao1 implements CinfoDao {

    public CinfoDao1() {
        System.out.println(11);
    }


    @Autowired
    CinfoDao dao;
    @Autowired
    CinfoDao dao1;
    @Override
    public int update(Cinfo cinfo) {

        return 0;
    }
}
