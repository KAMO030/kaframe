package kamo.idal;







import java.util.List;
import java.util.Map;

public class Configuration {

    private String defaultEnvironment;
    private Map<String,Environment> environments;
    private List<Mapper> mappers;

    public Configuration() {

    }

    public String getDefaultEnvironment() {
        return defaultEnvironment;
    }

    public void setDefaultEnvironment(String defaultEnvironment) {
        this.defaultEnvironment = defaultEnvironment;
    }
    public Map<String, Environment> getEnvironments() {
        return environments;
    }

    public void setEnvironments(Map<String, Environment> environments) {
        this.environments = environments;
    }

    public void setMappers(List<Mapper> mappers) {
        this.mappers = mappers;
    }

    public List<Mapper> getMappers() {
        return mappers;
    }


}
