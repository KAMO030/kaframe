package ioc_test.service.imp;

import com.kamo.context.annotation.Autowired;
import com.kamo.context.annotation.Component;
import com.kamo.context.annotation.Lazy;
import ioc_test.dao.CinfoDao;
import ioc_test.service.CinfoService;
import javafx.scene.effect.Bloom;

@Component
public class CinfoServiceImp implements CinfoService {


    private CinfoDao cinfoDao;
    private CinfoDao cinfoDao1;
    private CinfoService service;

    public CinfoServiceImp(CinfoDao cinfoDao) {
        this.cinfoDao = cinfoDao;
    }
    public CinfoServiceImp(){
        System.out.println(222);
    }
    @Autowired
    @Lazy
    public void setCinfoDao(CinfoDao cinfoDao) {
        this.cinfoDao = cinfoDao;
    }
    @Autowired

    public void setCinfoDao1(CinfoDao cinfoDao1) {
        this.cinfoDao1 = cinfoDao1;
    }
    @Autowired

    public void setService(CinfoService service) {
        this.service = service;
    }


    public int service() {
        System.out.println(service);
        System.out.println(cinfoDao1);
        System.out.println(cinfoDao);
//        System.out.println(cinfoDao.update(null));
        return 1;
    }
}
