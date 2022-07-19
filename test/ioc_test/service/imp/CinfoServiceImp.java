package ioc_test.service.imp;

import com.kamo.context.annotation.Autowired;
import com.kamo.context.annotation.Component;
import ioc_test.dao.CinfoDao;
import ioc_test.service.CinfoService;

@Component
public class CinfoServiceImp implements CinfoService {
    @Autowired
    private CinfoDao cinfoDao;
    public int service(){
        return cinfoDao.update(null);
    }
}
