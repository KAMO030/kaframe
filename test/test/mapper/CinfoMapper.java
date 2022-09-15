package test.mapper;

import com.kamo.jdbc.mapper_support.MapperSupport;
import com.kamo.jdbc.mapper_support.annotation.SQL;
import mapper_test.test.pojo.Cinfo;
import mapper_test.test.pojo.Ctype;
import mapper_test.test.vo.CinfoVO;

import java.util.List;
import java.util.Map;

public interface CinfoMapper extends MapperSupport<Cinfo> {
    @SQL("select cinfo.*,ctype.tname,cunit.uname from cinfo,ctype,cunit where cinfo.tid=ctype.tid and cinfo.uid=cunit.uid")
    List<Map> findAllCinfoVOByMap();
    @SQL("select cinfo.*,ctype.tname,cunit.uname from cinfo,ctype,cunit where cinfo.tid=ctype.tid and cinfo.uid=cunit.uid")
    Map findCinfoVOByMap();
    @SQL("select cinfo.*,ctype.tname,cunit.uname from cinfo,ctype,cunit where cinfo.tid=ctype.tid and cinfo.uid=cunit.uid")
    List<CinfoVO> findAllCinfoVOByList();
    @SQL("select cinfo.*,ctype.tname,cunit.uname from cinfo,ctype,cunit where cinfo.tid=ctype.tid and cinfo.uid=cunit.uid")
    CinfoVO findCinfoVOByObject();

    //sql语句太长了就用了配置文件方式
    @SQL
    CinfoVO findCinfoVOByCondition(Cinfo cinfo , String cNo, Ctype ctype);
    @SQL("update cinfo set cNo=? where 1=1 ${ and $ = ? }")
    Integer updateCinfoRInteger(String cNo,Cinfo cinfo);
    @SQL("update cinfo set cNo=? where cId = ?")
    Boolean updateCinfoRBoolean(String cNo, String cId);

    @SQL("select cName from cinfo")
    List<String> findCinfoAllcName();
    @SQL("select cName from cinfo")
    String findCinfoOnecName();
}
