package com.kamo.jdbc.mapper_upport;


import com.kamo.context.FactoryBean;
import com.kamo.context.annotation.Autowired;

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
