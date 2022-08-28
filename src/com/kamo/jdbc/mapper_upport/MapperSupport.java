package com.kamo.jdbc.mapper_upport;

import com.kamo.jdbc.RowMapper;

import java.util.List;

public interface MapperSupport<T> {


    /**
     * 通过自动拼接Sql添加一个Mapper实例所泛型出来的类型的entity，主键不需要设置，自动随机获取
     * 例如：（CustomerMapperImp所泛型的是Customer类型，所以就是把传入此方法的Customer实例对象添加到数据库的Customer表）
     * @param entity 要添加的泛型类型的实例
     * @return 影响的条数
     */
    int insert(T entity);
    /**
     * 通过自动拼接Sql进行批量新增
     * 例如：（CustomerMapperImp所泛型的是Customer类型，所以就是把传入此方法的Customer实例对象添加到数据库的Customer表）
     * @param entities 要添加的泛型类型的实例
     * @return 影响的条数
     */
    int insertByList(List<T> entities);
    /**
     * 通过自动拼接Sql删除一个Mapper实例所泛型出来的类型所对应的表的数据
     * 会通过传入的entity的每个属性来自动拼接sql的where条件，
     * 属性当不为null时，自动拼接属性名对应的where条件
     * @param entity 要删除的泛型类型的实例
     * @return 影响的条数
     */
    int deleteById(T entity);

    /**
     * 通过自动拼接Sql更新一个Mapper实例所泛型出来的类型所对应的表的数据
     * 会通过传入的entity的主键为where条件
     * 传入的entity除主键以外，其他属性的值不为null时自动拼接set后的sql语句；
     * @param entity 要更新的泛型类型的实例
     * @return 影响的条数
     */
    int updateById(T entity);
    /**
     * 通过自动拼接Sql查Mapper实例所泛型出来的类型的所有实例对象
     * @return Mapper实例所泛型出来的类型的所有实例对象
     */
    List<T> queryAll();
    /**
     * 通过自动拼接Sql查询Mapper实例所泛型出来的类型所对应对象集合
     * 会通过传入的entity的每个属性来自动拼接sql的where条件，
     * 属性当不为null时，自动拼接属性名对应的where条件
     * @param entity 要匹配的等于条件
     * @return dao实例所泛型出来的类型所对应对象集合
     */
    List<T> query(T entity);
    /**
     * 通过自动拼接Sql查询Mapper实例所泛型出来的类型所对应对象集合
     * 会通过传入的entity的每个属性来自动拼接sql的where条件，
     * 属性当不为null时，自动拼接属性名对应的where条件
     * @param equalEntity 要匹配的等于条件
     * @param likeEntity 要匹配的like条件
     * @return dao实例所泛型出来的类型所对应对象集合
     */
    List<T> query(T equalEntity,T likeEntity);

    /**
     * 通过自动拼接Sql按分页查询
     * @param startPage 查询从第几个开始
     * @param pageSize 查多少个
     * @return dao实例所泛型出来的类型所对应对象集合
     */
    List<T> queryByLimit(Integer startPage, Integer pageSize);
    /**
     * 通过自动拼接Sql按分页查询查询Mapper实例所泛型出来的类型所对应对象集合
     * 会通过传入的entity的每个属性来自动拼接sql的where条件，
     * 属性当不为null时，自动拼接属性名对应的where条件
     * @param equalEntity 要匹配的等于条件
     * @param likeEntity 要匹配的like条件
     * @param startPage 查询从第几个开始
     * @param pageSize 查多少个
     * @return dao实例所泛型出来的类型所对应对象集合
     */
    List<T> queryByLimitAndEntity(Integer startPage, Integer pageSize , T equalEntity, T likeEntity);
    /**
     * 通过自动拼接Sql查询Mapper实例所泛型出来的类型所对应第一个对象
     * 会通过传入的entity的每个属性来自动拼接sql的where条件
     * 属性当不为null时，自动拼接属性名对应的where条件
     * @param entity 要查询的泛型类型的实例
     * @return dao实例所泛型出来的类型所对应对象，没有则返回null
     */
    T queryForObject(T entity);
    /**
     * 通过自动拼接Sql查询一个Mapper实例所泛型出来的类型的主键等于传入值的对象
     * @param id 主键
     * @return 传入的主键存在时返回Mapper实例所泛型出来的类型的唯一实例，不存在返回null;
     */
    T queryById(Object id);
    /**
     * 通过自动拼接Sql查询多个Mapper实例所泛型出来的类型的主键等于传入值的对象
     * @param ids 主键
     * @return 传入的主键存在时返回Mapper实例所泛型出来的类型的唯一实例，不存在返回null;
     */
    List<T> queryByIds(Object... ids);
    /**
     * 通过自动拼接Sql查询多个Mapper实例所泛型出来的类型的主键等于传入值的对象
     * @param ids 主键
     * @return 传入的主键存在时返回Mapper实例所泛型出来的类型的唯一实例，不存在返回null;
     */
    List<T> queryByIds(List ids);
    /**
     * 通过自动拼接Sql查询一个Mapper实例所泛型出来的类型的主键等于传入值的外键字段相同名字的对象
     * 比如此Mapper的实现子类为商品类型，传入的primaryEntity为商品表对象
     * 那么他会根据商品类型的主键名拿到传入的商品表对象中相同属性字段的值调用queryById方法
     * @param primaryEntity 主表对象
     * @return 主表对象的外键属性字段作为此Mapper主键查寻存在时返回Mapper实例所泛型出来的类型的唯一实例，不存在返回null;
     */
    T queryAsForeignKey(Object primaryEntity);
    /**
     * 查询表中的数据条数
     *
     * @return Mapper实例所泛型出来类型对应的表中的数据条数
     */
    Integer count();
    /**
     * 根据传入的sql和参数列表查询
     * @param sql sql语句
     * @param params 参数列表
     * @return 查询到的Mapper实例所泛型出来类型的实例对象集合
     */
    List<T> query(String sql,Object ... params);
    List<T> query(String sql, RowMapper<T> rowMapper,Object ... params);
    /**
     * 根据传入的sql和参数列表更新表数据
     * @param sql sql语句
     * @param params 参数列表
     * @return 受影响的行数
     */
    int update(String sql, Object ... params);
    /**
     * 根据传入的sql和参数列表查询
     * @param sql sql语句
     * @param params 参数列表
     * @return 查询到的Mapper实例所泛型出来类型的一个实例对象，没有则返回null
     */
    T queryForObject(String sql,Object ... params);

}
