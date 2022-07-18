package basedao_test.service.imp;

import basedao_test.dao.CinfoDao;
import basedao_test.pojo.Cinfo;
import basedao_test.service.CinfoService;
import basedao_test.vo.CinfoVO;
import com.kamo.context.annotation.Autowired;
import com.kamo.context.annotation.Component;

import java.util.List;

@Component
public class CinfoServiceImp implements CinfoService {
    @Autowired
    private CinfoDao cinfoDao;
    public List<CinfoVO> service(Cinfo cinfo){
        return cinfoDao.selectCinfoVoList(cinfo);
    }
}
