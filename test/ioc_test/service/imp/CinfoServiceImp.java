package ioc_test.service.imp;

import com.kamo.bean.annotation.Autowired;
import com.kamo.bean.annotation.Bean;
import com.kamo.bean.annotation.Component;
import com.kamo.context.condition.Condition;
import com.kamo.core.support.AnnotationMetadata;
import ioc_test.dao.CinfoDao;
import ioc_test.service.CinfoService;

import java.lang.reflect.AnnotatedElement;

@Component
public class CinfoServiceImp implements CinfoService {



    private CinfoDao cinfoDao;

    private CinfoDao cinfoDao1;
    private CinfoService service;

    public CinfoServiceImp(CinfoDao cinfoDao) {
        this.cinfoDao = cinfoDao;
    }

    public CinfoServiceImp() {
        System.out.println(222);
    }

    @Autowired
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

    public void destroy(){
    System.out.println("销毁");
    }
    public int service() {
        System.out.println(service+"1111");
        System.out.println(cinfoDao1);
        System.out.println(cinfoDao);
        System.out.println(cinfoDao.update(null));
        return 1;
    }

    public static class ConditionTest implements Condition {

        @Override
        public boolean matches(AnnotationMetadata metadata, AnnotatedElement element) {
            System.out.println(metadata.hasAnnotation(Bean.class));
            return false;
        }
    }
}

