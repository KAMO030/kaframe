package basedao_test.dao;

import basedao_test.pojo.Cinfo;
import basedao_test.vo.CinfoVO;
import com.kamo.jdbc.basedao.BaseDao;
import com.kamo.jdbc.basedao.SQL;

import java.util.List;


public interface CinfoDao extends BaseDao<Cinfo> {

    @SQL
    List<CinfoVO> selectCinfoVoList(Cinfo cinfo);
    @SQL
    Integer count(Cinfo cinfo);
    @SQL
    Integer u(Cinfo cinfo);

    @SQL
    List<String> queryCname(Cinfo cinfo);


}
