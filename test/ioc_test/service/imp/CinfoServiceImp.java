package ioc_test.service.imp;

import com.kamo.context.annotation.Autowired;
import com.kamo.context.annotation.Component;
import ioc_test.dao.CinfoDao;
import ioc_test.service.CinfoService;

@Component
public class CinfoServiceImp implements CinfoService {
    @Autowired
    private CinfoDao cinfoDao;

    @Autowired
    public CinfoServiceImp(CinfoDao cinfoDao) {
        this.cinfoDao = cinfoDao;
    }

    public CinfoServiceImp() {
    }

    public int service() {
        return cinfoDao.update(null);
    }
}
