package com.kamo.jdbc.mapper_support;


import com.kamo.bean.annotation.Autowired;
import com.kamo.context.factory.FactoryBean;

public class MapperSupportFactoryBean implements FactoryBean {

    @Autowired
    private MapperSupportFactory mapperSupportFactory;

    @Override
    public Object getObject(Class mapperClass) {
        return mapperSupportFactory.getMapper( mapperClass);
    }

    @Override
    public Class getObjectType() {
        return MapperSupport.class;
    }


}
