package kamo.idal;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlSessionFactory {
    private Configuration configuration;

    public SqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }
    public SqlSession openSession(){
        DataSource dataSource = configuration.getEnvironments().
                get(configuration.getDefaultEnvironment()).getDataSource();
        Map<String,Mapper> mapperMap = new HashMap<>();
        List<Mapper> mappers = configuration.getMappers();
        for (Mapper mapper : mappers) {
            mapperMap.put(mapper.getMapperClass(),mapper);
        }
        return new SqlSession(dataSource,mapperMap);
    }
}
