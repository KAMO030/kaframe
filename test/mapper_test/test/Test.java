package mapper_test.test;

import com.kamo.context.factory.ApplicationContext;
import com.kamo.context.annotation.support.AnnotationConfigApplicationContext;
import com.kamo.bean.annotation.Autowired;
import com.kamo.jdbc.mapper_support.IPage;
import com.kamo.jdbc.mapper_support.PageHelper;
import mapper_test.test.mapper.CinfoMapper;
import mapper_test.test.service.CinfoService;
import mapper_test.test.vo.CinfoVO;

import javax.imageio.IIOParam;
import java.util.*;

public class Test {

     @Autowired
     private static List<String> name;
    public static void main(String[] args) throws NoSuchFieldException {
        ApplicationContext context = new AnnotationConfigApplicationContext(Test.class,Config.class);
//        CinfoService service = context.getBean("cinfoServiceImpl");
//        service.service();
        CinfoMapper bean = context.getBean(CinfoMapper.class);
        PageHelper.setPage(444,2);
        Boolean iPage =  bean.updateCinfoRBoolean("123", "c001");
        System.out.println(iPage);
//        CinfoVO cinfoVO = iPage.get(0);
//        iPage.forEach(c-> System.out.println(c));
//        System.out.println(iPage);
//        System.out.println(bean);

//        使用原生方法查单表所有
//        System.out.println(bean.queryAll());
////        查所有返回List<Map>
//        System.out.println(bean.findAllCinfoVOByMap());
////        查一个对象返回Map
//        System.out.println(bean.findCinfoVOByObject());
////        查所有返回List<CinfoVO>
//        System.out.println(bean.findAllCinfoVOByList());
//        //查一个对象返回CinfoVO
//        System.out.println(bean.findCinfoVOByObject());
//        Cinfo cinfo = new Cinfo();
//        Ctype ctype = new Ctype();
//        cinfo.setuId("u001");
//        cinfo.setcSpace("300ml");
//        ctype.settName("饮");
////        查询uId(单位ID)等于u001并且cSpace(规格)为300ml，并且cNo(备注)为‘无’，并且模糊条件tName(类型名)里有'饮'字的商品
////        select cinfo.*,ctype.tname,cunit.uname
////        from cinfo,ctype,cunit
////        where cinfo.tid=ctype.tid and cinfo.uid=cunit.uid
////        ${ and cinfo.$  = ? } and cNo=? ${ and ctype.$  like ? }
//        System.out.println(bean.findCinfoVOByCondition(cinfo,"0",ctype));
//        //执行更新自定识别返回Integer类型
//        System.out.println(bean.updateCinfoRInteger("haha",cinfo));
////        执行更新自定识别返回Boolean类型
//        System.out.println(bean.updateCinfoRBoolean("haha","c001"));
//        System.out.println(bean.queryAll());
//        //查询多条单字段数据
//        System.out.println(bean.findCinfoAllcName());
////        //查询单条单字段数据
//        System.out.println(bean.findCinfoOnecName());

    }
    private static String test(String cNo, String cId,List parms){
        String sql = "update cinfo set cNo=? where cId = ?";
        parms.add(cNo);
        parms.add(cId);
        return sql;
    };
}
