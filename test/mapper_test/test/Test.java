package mapper_test.test;

import com.kamo.context.ApplicationContext;
import com.kamo.context.annotation.AnnotationConfigApplicationContext;
import com.kamo.context.annotation.Import;
import com.kamo.util.ReflectUtils;
import mapper_test.test.service.CinfoService;

public class Test {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        CinfoService service = context.getBean("cinfoServiceImpl");
//        CinfoMapper bean = context.getBean(CinfoMapper.class);
//        System.out.println(bean);
        service.service();

        System.out.println(ReflectUtils.getAnnotations(Config.class, Import.class).get(1).value()[0]);
        System.out.println(ReflectUtils.getAnnotation(Config.class, Import.class));
        System.out.println(ReflectUtils.getAnnotationMetadatas(Config.class,Import.class).size());

        //使用原生方法查单表所有
//        System.out.println(bean.queryAll());
        //查所有返回List<Map>
//        System.out.println(bean.findAllCinfoVOByMap());
//        //查一个对象返回Map
//        System.out.println(bean.findCinfoVOByMap());
//        //查所有返回List<CinfoVO>
//        System.out.println(bean.findAllCinfoVOByList());
//        //查一个对象返回CinfoVO
//        System.out.println(bean.findCinfoVOByObject());
//        Cinfo cinfo = new Cinfo();
//        Ctype ctype = new Ctype();
//        cinfo.setuId("u001");
//        cinfo.setcSpace("300ml");
//        ctype.settName("饮");
//        查询uId(单位ID)等于u001并且cSpace(规格)为300ml，并且cNo(备注)为‘无’，并且模糊条件tName(类型名)里有'饮'字的商品
//        select cinfo.*,ctype.tname,cunit.uname
//        from cinfo,ctype,cunit
//        where cinfo.tid=ctype.tid and cinfo.uid=cunit.uid
//        ${ and cinfo.$  = ? } and cNo=? ${ and ctype.$  like ? }
//        System.out.println(bean.findCinfoVOByCondition(cinfo,"0",ctype));
//        //执行更新自定识别返回Integer类型
//        System.out.println(bean.updateCinfoRInteger("haha",cinfo));
        //执行更新自定识别返回Boolean类型
//        System.out.println(bean.updateCinfoRBoolean("haha","c001"));
//        System.out.println(bean.queryAll());
//        //查询多条单字段数据
//        System.out.println(bean.findCinfoAllcName());
////        //查询单条单字段数据
//        System.out.println(bean.findCinfoOnecName());
    }
}
