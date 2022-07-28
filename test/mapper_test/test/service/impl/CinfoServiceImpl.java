package mapper_test.test.service.impl;

import com.kamo.context.annotation.Autowired;
import com.kamo.context.annotation.Service;
import com.kamo.transaction.annotation.Propagation;
import com.kamo.transaction.annotation.Transactional;
import mapper_test.test.mapper.CinfoMapper;
import mapper_test.test.pojo.Cinfo;
import mapper_test.test.service.CinfoService;

@Service
@Transactional
public class CinfoServiceImpl implements CinfoService {
    @Autowired
    CinfoMapper cinfoMapper;
    @Autowired
    CinfoService service;
    @Override
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public void service() {
        Cinfo cinfo = new Cinfo();
        cinfo.setcId("19f724cb20fd46b5be0f0584868069c0");
        cinfo.setcNo("1");
        System.out.println(cinfoMapper.update(cinfo));
        service.service1();


    }
    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public void service1() {
        Cinfo cinfo = new Cinfo();
        cinfo.setcId("19f724cb20fd46b5be0f0584868069c0");
        cinfo.setcName("2");
        try {
            int id = 1/0;

        }catch (Exception e) {
        }
        System.out.println(cinfoMapper.update(cinfo));
    }
}
