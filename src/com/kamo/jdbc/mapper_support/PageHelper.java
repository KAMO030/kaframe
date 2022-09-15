package com.kamo.jdbc.mapper_support;

import java.util.List;

public class PageHelper {
    private static ThreadLocal<IPage> iPageThreadLocal = new ThreadLocal<>();
    public static void setPage(Integer currentPage, Integer pageSize){
        iPageThreadLocal.set(new IPage(currentPage,pageSize));
    }
    public static void setPage(IPage page){
        iPageThreadLocal.set(page);
    }
    protected static IPage getPage(){
        return iPageThreadLocal.get();
    }
    protected static void removePage(){
         iPageThreadLocal.remove();
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
     * @param <T>
     * @param mapper      PageBean对应泛型类型的mapper类对象实例
     * @param page 根据 IPage对象的 CurrentPage 和 PageSize 进行分页 如果任意一个值为null则不进行分页
     * @param equalEntity 要匹配的等于条件
     * @param likeEntity  要匹配的like条件
     * @return 返回dao对应泛型的PageBean
     */
    public static <T> IPage<T> getIPage(MapperSupport<T> mapper, IPage<T> page , T equalEntity, T likeEntity){
        if (page!=null&&page.getCurrentPage()!=null&&page.getCurrentPage()!=null){
            page.setTotalCount(mapper.count());
            List<T> dataList = mapper.queryByLimitAndEntity(page.getStartPage(), page.getPageSize(), equalEntity,likeEntity);
            page.setDataList(dataList);
            return page;
        }
       throw new IllegalArgumentException();

    }
    public static <T> IPage<T> getIPage(MapperSupport<T> mapper, Integer currentPage, Integer pageSize , T entity){
        return  getIPage(mapper,new IPage<>(currentPage,pageSize),entity,null);
    }
    public static <T> IPage<T> getIPage(MapperSupport<T> mapper, Integer currentPage, Integer pageSize ){
        return  getIPage(mapper,new IPage<>(currentPage,pageSize),null,null);
    }
    public static <T> IPage<T> getIPage(MapperSupport<T> mapper, IPage<T> page ){
        return  getIPage(mapper,page,null,null);
    }
    public static <T> IPage<T> getIPage(MapperSupport<T> mapper, IPage<T> page , T entity){
        return  getIPage(mapper,page,entity,null);
    }
}
