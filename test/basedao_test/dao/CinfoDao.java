package basedao_test.dao;

import basedao_test.pojo.Cinfo;
import basedao_test.vo.CinfoVO;
import com.kamo.jdbc.basedao.BaseDao;
import com.kamo.jdbc.basedao.SQL;

import java.util.List;


public interface CinfoDao extends BaseDao<Cinfo> {
    @SQL("select cinfo.*,ctype.tname,cunit.uname " +
            "from cinfo,ctype,cunit  " +
            "where cinfo.tid=ctype.tid and cinfo.uid=cunit.uid" +
            "" +
            "${ and cinfo.$ like ? }")
    List<CinfoVO> selectCinfoVoList(Cinfo cinfo);
    @SQL("Select count(*) " +
            "from cinfo,ctype,cunit  " +
            "where cinfo.tid=ctype.tid and cinfo.uid=cunit.uid" +
            "${ and cinfo.$ = ? }" +
            "${ and cinfo.$ like ? }")
    Integer count(Cinfo cinfo);
    @SQL("update cinfo set cName = '可乐2' where 1=1 ${ and cinfo.$ like ? }")
    Integer u(Cinfo cinfo);


}
