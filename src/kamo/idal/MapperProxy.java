package kamo.idal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class MapperProxy implements InvocationHandler {
    private SqlSession sqlSession;
    private Mapper mapper;

    MapperProxy(Mapper mapper, SqlSession sqlSession) {
        this.sqlSession = sqlSession;
        this.mapper = mapper;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String id = method.getName();
        Class returnType = method.getReturnType();
        String mapperClass = mapper.getMapperClass() + "." + id;

        if (mapper.containsSelectByID(id)) {
            if (returnType.equals(List.class)) {
                return sqlSession.selectList(mapperClass, args);
            } else {
                return sqlSession.selectOne(mapperClass, args);
            }
        } else if (mapper.containsUpDateByID(id)) {
            if (returnType.equals(Integer.class) || returnType.equals(int.class)) {
                return sqlSession.updateRow(mapperClass, args);
            } else {
                return sqlSession.isUpdate(mapperClass, args);
            }
        } else {
            return this.getClass().getMethod(id).invoke(this, args);
        }
    }
}
