package commons.service;


import java.util.List;

public interface BaseService {
    /**
     * 查询所有该vo的类型的数据
     * @param voType 需要的vo的类型
     * @return 将查询结果存入vo的类型的list返回
     * @throws
     */
    List selectAllVOByList(Class voType);
    /**
     * 查询所有该vo的类型的数据
     * @param voType 需要的vo的类型
     * @return 将查询结果存入vo的类型的list返回
     * @throws
     */
    String selectAllVOByColumnNames(Class voType, String columnName, Object...values);
    /**
     * 按照主键id查询该vo的类型的数据
     * @param voType 需要的vo的类型
     * @param id 主键id
     * @return 返回查询到的vo类型的对象
     * @throws
     */
    String selectVOByID(Class voType, Object id)  ;

    /**
     * 按照主键id查询该vo的类型的数据
     * @param beanType 需要的表类的类型
     * @return 返回查询到的vo类型的对象
     * @throws
     */

    String selectAllByColumnNames(Class beanType, String columnName, Object...values) ;




}

