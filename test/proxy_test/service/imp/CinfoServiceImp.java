package proxy_test.service.imp;


import com.kamo.bean.annotation.Autowired;
import com.kamo.bean.annotation.Component;

@Component
public class CinfoServiceImp {
    @Autowired
    private CinfoServiceImp cinfoServiceImp;

    public int init;
    public String service(String service) {
        System.out.println(this);
        init =1;
        System.out.println(this.getIndex());
        System.out.println("执行了service方法:" + service);
        return cinfoServiceImp.service1("222");
    }

    public String service1(String service) {
        System.out.println(this);
        System.out.println("执行了service1方法:" + service);
        return "Hello";
    }


    public String getIndex() {
        return init+"";
    }


}
