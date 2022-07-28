package test.service.impl;


import com.kamo.context.annotation.Autowired;
import mapper_test.test.mapper.CinfoMapper;
import mapper_test.test.service.CinfoService;

public class CinfoServiceImpl implements CinfoService {
    @Autowired
    CinfoMapper cinfoMapper;
    @Override
    public void service() {
        System.out.println(cinfoMapper);
        System.out.println(cinfoMapper.findAllCinfoVOByList());
    }

    @Override
    public void service1() {

    }
}
