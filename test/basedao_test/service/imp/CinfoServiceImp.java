package basedao_test.service.imp;

import basedao_test.dao.CinfoDao;
import basedao_test.pojo.Cinfo;
import basedao_test.service.CinfoService;
import com.kamo.bean.annotation.Autowired;
import com.kamo.bean.annotation.Component;
import com.kamo.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class CinfoServiceImp implements CinfoService {
    @Autowired
    private CinfoDao cinfoDao;

    public List<String> service(Cinfo cinfo){
        return cinfoDao.queryCname(cinfo);
    }

}
