package com.kamo.jdbc.basedao;

import com.kamo.context.FactoryBean;

import javax.sql.DataSource;
@Deprecated

public class BaseDaoFactoryBean extends BaseDaoFactory implements FactoryBean {
    public BaseDaoFactoryBean(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Object getObject(Class daoClass) {
        if (!BaseDao.class.isAssignableFrom(daoClass)) {
            throw new IllegalArgumentException(daoClass +" 类型没有继承于: "+BaseDao.class);
        }
        return getBaseDao( daoClass);
    }


    @Override
    public Class getObjectType() {
        return BaseDao.class;
    }
}
