package kamo.idal;

import javax.sql.DataSource;

public class Environment {
    private String id;
    private DataSource dataSource;

    public Environment(String id, DataSource dataSource) {
        this.id = id;
        this.dataSource = dataSource;
    }

    public Environment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
