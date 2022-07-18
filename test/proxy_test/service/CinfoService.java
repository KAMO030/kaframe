package proxy_test.service;

import basedao_test.pojo.Cinfo;
import basedao_test.vo.CinfoVO;
import com.kamo.context.annotation.Component;

import java.util.List;
public interface CinfoService {
    String service(String service);
    String service1(String service);
}
