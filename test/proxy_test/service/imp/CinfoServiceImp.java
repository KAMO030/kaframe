package proxy_test.service.imp;


import com.kamo.context.annotation.Autowired;
import com.kamo.context.annotation.Component;
import proxy_test.service.CinfoService;

@Component
public class CinfoServiceImp implements CinfoService {
    @Autowired
    private CinfoService cinfoServiceImp;

    public String service(String service) {
        System.out.println(this);
        System.out.println(cinfoServiceImp);
        System.out.println("执行了service方法:" + service);
        return cinfoServiceImp.service1("222");
    }

    public String service1(String service) {
        System.out.println(this);
        System.out.println("执行了service1方法:" + service);
        return "Hello";
    }
}
