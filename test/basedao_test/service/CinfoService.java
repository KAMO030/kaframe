package basedao_test.service;

import basedao_test.service.pojo.Cinfo;
import basedao_test.vo.CinfoVO;

import java.util.List;

public interface CinfoService {
    List<CinfoVO> service(Cinfo cinfo);
}
