package mapper_test.test.service.impl;

import com.kamo.context.annotation.Autowired;
import com.kamo.context.annotation.Lazy;
import com.kamo.context.annotation.Service;
import com.kamo.context.factory.InitializingBean;
import com.kamo.transaction.annotation.Propagation;
import com.kamo.transaction.annotation.Transactional;
import mapper_test.test.mapper.CinfoMapper;
import mapper_test.test.pojo.Cinfo;
import mapper_test.test.service.CinfoService;

import java.util.List;

@Service
@Transactional
public class CinfoServiceImpl implements CinfoService {
    @Autowired
    CinfoMapper cinfoMapper;
    @Autowired
//            @Lazy
    CinfoService service;
    @Override
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void service() {
        Cinfo cinfo = new Cinfo();
        cinfo.setcId("3123123213123");
        cinfo.setcName("可怜人11");
        cinfo.setcNo("1");
        cinfoMapper.insert(cinfo);
        List<Cinfo> cinfoList = cinfoMapper.queryAll();
        for (Cinfo cinfo1 : cinfoList) {
            System.out.println(cinfo1);
        }

        service.service1();


    }
    @Override
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void service1() {
        Cinfo cinfo = new Cinfo();
        System.out.println(this.equals(service));
        System.out.println(service);
        cinfo.setcId("19f724cb20fd46b5be0f0584868069c0");
        cinfo.setcName("2");
        int id = 1/0;
        System.out.println(cinfoMapper.updateById(cinfo));
    }

}
