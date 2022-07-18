package com.kamo.util;




import com.kamo.jdbc.PageBean;
import com.kamo.jdbc.basedao.BaseDao;
import javafx.util.converter.DateStringConverter;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BeanUtils {
    private static final Map<Class, Converter> converterMap = new HashMap<>();
    static {
        converterMap.put(Integer.class, values -> Integer.valueOf((String) values[0]));
        converterMap.put(int.class, values -> Integer.parseInt((String) values[0]));
        converterMap.put(Date.class, values -> new DateStringConverter(DateFormat.DEFAULT).fromString((String) values[0]));
        converterMap.put(Boolean.class, values -> Boolean.parseBoolean((String) values[0]));
        converterMap.put(String.class, values -> values[0]);
        converterMap.put(String[].class, values -> values);

    }
    private BeanUtils() {
    }

    /**
     * 根据Map中的key给相同属性字段名赋值
     * @param obj 需要自动赋值的对象
     * @param parameterMap Map<String, String[]>类型
     * @return 返回自动赋值后的对象
     */
    public static Object autoAllocation(Object obj , Map<String, String[]> parameterMap){

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            String name = field.getName();
            String[] values = parameterMap.get(name);
            field.setAccessible(true);
            if (values==null||values.length == 0||values[0].equals("")) {
                continue;
            }
            Class type = field.getType();
            Object value = converterMap.get(type).convert(values);
            try {
                field.set(obj,value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return obj;
    }

    /**
     * 根据数据总条数和每页显示多少数据获取一共多少页
     * @param totalCount 数据总条数
     * @param pageSize 每页显示几条数据
     * @return 总页数
     */
   public static Integer getTotalPage(Integer totalCount,Integer pageSize){
       return totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;
    }

    /**
     * 根据数据当前页数和每页显示多少数据获取当前页是从第几条数据开始（）
     * @param currentPage
     * @param pageSize
     * @return StartPage返回从第几条数据开始
     */
    public static Integer getStartPage(Integer currentPage,Integer pageSize){
        return  (currentPage - 1) * pageSize;
    }

    /**
     *
     * @param dao PageBean对应泛型类型的dao类对象实例
     * @param currentPage 当前第几页
     * @param pageSize 每页显示几条数据
     * @param equalEntity 要匹配的等于条件
     * @param likeEntity 要匹配的like条件
     * @return 返回dao对应泛型的PageBean
     * @param <T>
     */
    public static <T> PageBean<T> pageBean(BaseDao<T> dao, Integer currentPage, Integer pageSize , T equalEntity, T likeEntity){
        Integer totalCount = dao.count();
        Integer totalPage = getTotalPage(totalCount,pageSize);
        List<T> datas = dao.queryByLimitAndEntity(getStartPage(currentPage,pageSize), pageSize, equalEntity,likeEntity);
        return  new PageBean<T>(datas,currentPage, pageSize,totalPage ,totalCount);
    }
    public static <T> PageBean<T> pageBean(BaseDao<T> dao,Integer currentPage, Integer pageSize ,T entity){
        return  pageBean(dao,currentPage,pageSize,entity,null);
    }
    public static <T> PageBean<T> pageBean(BaseDao<T> dao,Integer currentPage, Integer pageSize ){
        return  pageBean(dao,currentPage,pageSize,null,null);
    }
}
