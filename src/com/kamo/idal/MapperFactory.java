package com.kamo.idal;

import com.kamo.util.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class MapperFactory {

    public static Mapper creatMapper(String path) {

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        Document doc = null;
        Mapper mapper = new Mapper();
        mapper.setResource(path);
        try {
            documentBuilder = builderFactory.newDocumentBuilder();
            doc = documentBuilder.parse(Resource.getResourceAsStream(path));
            Element mapperEle = (Element) doc.getElementsByTagName("mapper").item(0);
            mapper.setMapperClass(mapperEle.getAttributeNode("namespace").getValue());
            mapper.setSelectMap(creatSelectMap(doc.getElementsByTagName("select")));
            mapper.setUpDateMap(creatUpdateMap(doc));
            mapper.setResultMap(creatResultMap(doc.getElementsByTagName("resultMap")));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return mapper;
    }

    private static Map<String, ResultMap> creatResultMap(NodeList resultMapList) throws ClassNotFoundException {
        Map<String,ResultMap> resultMapMap = new HashMap<>();
        int length = resultMapList.getLength();
        NodeList resultList;
        Element resultMapEle;
        Element resultEle;
        ResultMap resultMap ;
        String id;
        Class resultType;
        String column;
        String property;
        Map<String, String> columnMap;
        for (int i=0;i<length;i++){
            resultMapEle = (Element) resultMapList.item(i);
            resultType = Class.forName(resultMapEle.getAttribute("resultType")) ;
            id = resultMapEle.getAttribute("id");
            columnMap = new HashMap<>();
            resultList = resultMapEle.getElementsByTagName("result");
            for (int j = 0; j< resultList.getLength(); j++){
                resultEle = (Element) resultList.item(j);
                column = resultEle.getAttribute("column").toUpperCase();
                property = resultEle.getAttribute("property");
                columnMap.put(column,property);
            }
            resultMap = new ResultMap(columnMap,resultType);
            resultMapMap.put(id,resultMap);
        }
        return resultMapMap;
    }

    private static Map creatUpdateMap(Document document) throws ClassNotFoundException {
        Map updateMap = new HashMap();
        NodeList updateList = document.getElementsByTagName("update");
        NodeList deleteList = document.getElementsByTagName("delete");
        NodeList insertList = document.getElementsByTagName("insert");
        String sql = null;
        String id = null;
        String paramType = null;
        Element itemEle;
        Map<String, Object> map;
        int updateListLength = updateList.getLength();
        int deleteListLength = deleteList.getLength();
        int insertListLength = insertList.getLength();
        for (int i = 0; i < updateListLength; i++) {
            itemEle = (Element) updateList.item(i);
            id = itemEle.getAttribute("id");
            paramType = itemEle.getAttribute("paramType");
            sql = itemEle.getTextContent().trim();
            List paraNames = new ArrayList();
            sql= getParameters(paraNames,sql);
            map = new HashMap();
            map.put("sql", sql);
            map.put("paramType",paramType);
            map.put("paraNames", paraNames);
            updateMap.put(id, map);
        }
        for (int i = 0; i < deleteListLength; i++) {
            itemEle = (Element) deleteList.item(i);
            id = itemEle.getAttribute("id");
            paramType = itemEle.getAttribute("paramType");
            sql = itemEle.getTextContent().trim();
            List paraNames = new ArrayList();
            sql= getParameters(paraNames,sql);
            map = new HashMap();
            map.put("paramType",paramType);
            map.put("sql", sql);
            map.put("paraNames", paraNames);
            updateMap.put(id, map);
        }
        for (int i = 0; i < insertListLength; i++) {
            itemEle = (Element) insertList.item(i);
            id = itemEle.getAttribute("id");
            paramType = itemEle.getAttribute("paramType");
            sql = itemEle.getTextContent().trim();
            List paraNames = new ArrayList();
            sql= getParameters(paraNames,sql);
            map = new HashMap();
            map.put("paramType",paramType);
            map.put("sql", sql);
            map.put("paraNames", paraNames);
            updateMap.put(id, map);
        }
        return updateMap;
    }

    private static Map creatSelectMap(NodeList selectList) throws ClassNotFoundException {
        Map selectMap = new HashMap();
        int length = selectList.getLength();
        Element selectItemEle;
        String sql;
        String id;
        String paramType;
        String select;
        Class resultType;
        String resultMap;
        Map<String, Object> map;
        for (int i = 0; i < length; i++) {
            selectItemEle = (Element) selectList.item(i);
            id = selectItemEle.getAttribute("id");
            paramType = selectItemEle.getAttribute("paramType");
            select = selectItemEle.getAttribute("select");
            String resultTypePath = selectItemEle.getAttribute("resultType");
            map = new HashMap();
            if (resultTypePath.isEmpty()) {
                resultMap =selectItemEle.getAttribute("resultMap");
                map.put("resultMap", resultMap);
            }else {
                resultType = Class.forName(resultTypePath);
                map.put("resultType", resultType);
            }
            sql = selectItemEle.getTextContent().trim();
            List paraNames = new ArrayList();
            sql= getParameters(paraNames,sql);
            map.put("select",select );
            map.put("sql", sql);
            map.put("paraNames", paraNames);
            map.put("paramType", paramType);
            selectMap.put(id, map);
        }
        return selectMap;
    }

    private static String getParameters(List paraNames , String sql) {
        String paraName;
        int begin = 0;
        int end = 0;
        while ((begin = sql.indexOf("#{")) != -1) {
            end = sql.indexOf("}");
            paraName = sql.substring(begin + 2, end);
            paraNames.add(paraName);
            sql = sql.replaceAll("#\\{" + paraName + "}", "?");
        }
        return sql;
    }
}
