package com.kamo.jdbc.basedao;

import com.kamo.datasource.IzumiDataSource;
import com.kamo.datasource.IzumiDataSourceFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * 构造BaseDaoFactory的类
 */
public final class BaseDaoFactoryBuild {
    private BaseDaoFactoryBuild() {

    }

    /**
     * 根据用户自己加载完文件后的Properties对象,构造BaseDaoFactory对象
     * @param props 用户自己加载完文件后的Properties对象
     * @return BaseDaoFactory对象
     */
    public static BaseDaoFactory build(Properties props){
        IzumiDataSource dataSource = IzumiDataSourceFactory.createDataSource(props);
        return new BaseDaoFactory(dataSource);
    }

    /**
     * 根据路径使用Properties对象读取路径对应的文件后将Properties传入 #build(Properties props)方法
     * @param path 文件路径
     * @return BaseDaoFactory对象
     */
    public static BaseDaoFactory build(String path){
        //如果用户只给了文件路径,那就我们自己new一个Properties对象并读取文件
        Properties props = new Properties();
        try {
            props.load(BaseDaoFactoryBuild.class.getClassLoader().getResourceAsStream(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return build(props);
    }
}
