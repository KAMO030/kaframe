package com.kamo.idal;

import java.util.Map;

public class Mapper {
    private String resource;
    private String mapperClass;
    private Map<String, Map> selectMap;
    private Map<String, Map> upDateMap;
    private Map<String, ResultMap> resultMap;

    public Map getSelectByID(String id) {
        return selectMap.get(id);
    }
    public Map getUpDateByID(String id) {
        return upDateMap.get(id);
    }
    public ResultMap getResultMapByID(String id) {
        return resultMap.get(id);
    }

    public void setResultMap(Map<String, ResultMap> resultMap) {
        this.resultMap = resultMap;
    }

    public boolean containsResultMapByID(String id) {
        return resultMap.containsKey(id);
    }

    public boolean containsSelectByID(String id) {
        return selectMap.containsKey(id);
    }
    public boolean containsUpDateByID(String id) {
        return upDateMap.containsKey(id);
    }
    public void setSelectMap(Map<String, Map> selectMap) {
        this.selectMap = selectMap;
    }

    public Map<String, Map> getUpDateMap() {
        return upDateMap;
    }

    public void setUpDateMap(Map<String, Map> upDateMap) {
        this.upDateMap = upDateMap;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }


    public String getMapperClass() {
        return mapperClass;
    }

    public void setMapperClass(String mapperClass) {
        this.mapperClass = mapperClass;
    }

    @Override
    public String toString() {
        return resource+mapperClass;
    }
}
