package kamo.idal;

import kamo.datasource.IzumiDataSource;
import kamo.util.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(String path){
        return build(SqlSessionFactoryBuilder.class.getClassLoader().getResourceAsStream(path));
    }
    public SqlSessionFactory build(InputStream inputStream) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        Document doc = null;
        Configuration configuration = null;
        try {
            documentBuilder = builderFactory.newDocumentBuilder();
            doc = documentBuilder.parse(inputStream);
            configuration = new Configuration();

            Node environments = doc.getElementsByTagName("environments").item(0);
            Element environmentsEle = (Element) environments;
            //设置默认的Environment的id名
            configuration.setDefaultEnvironment(getDefaultEnvironment(environmentsEle));

            NodeList environmentList = doc.getElementsByTagName("environment");
            NodeList mappreList = doc.getElementsByTagName("mapper");

            //遍历environments,获得environmentMap;
            configuration.setEnvironments(creatEnvironmentMap(environmentList));
            //遍历mapper节点,获得所有的mapper并封装到List里;
            configuration.setMappers(creatMapperList(mappreList));
        } catch (NoSuchFieldException | ClassNotFoundException | InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }finally {
            try {
                if (inputStream!=null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new SqlSessionFactory(configuration);
    }

    private List<Mapper> creatMapperList(NodeList mappreList) throws ClassNotFoundException {
        List<Mapper> mappers = new ArrayList<>();
        int length = mappreList.getLength();
        Class mapperClass = Mapper.class;
        String value;
        Element mappreEle;
        for (int i = 0; i < length; i++) {
            mappreEle = (Element) mappreList.item(i);
            if (mappreEle.hasAttribute("resource")) {
                value = mappreEle.getAttribute("resource");
                mappers.add(MapperFactory.creatMapper(value));
            } else if (mappreEle.hasAttribute("class")) {
                value = mappreEle.getAttribute("class");
                String resource = value.replace(".", "/") + ".xml";
                mappers.add(MapperFactory.creatMapper(resource));
            } else if (mappreEle.hasAttribute("package")) {

            }
        }
        return mappers;
    }

    private String getDefaultEnvironment(Element environmentsEle) {
        if (!environmentsEle.hasAttribute("default")) {
            throw new RuntimeException();
        }
        return environmentsEle.getAttribute("default");
    }

    private Map<String, Environment> creatEnvironmentMap(NodeList environmentList) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Map<String, Environment> environmentMap = new HashMap();
        int length = environmentList.getLength();
        for (int i = 0; i < length; i++) {
            Element environmentEle = (Element) environmentList.item(i);
            if (!environmentEle.hasAttribute("id")) {
                throw new RuntimeException();
            }
            String id = environmentEle.getAttribute("id");
            DataSource dataSource = creatDataSource(environmentEle.getElementsByTagName("dataSource").item(0));
            Environment environment = new Environment(id, dataSource);
            environmentMap.put(id, environment);
        }
        return environmentMap;
    }

    private DataSource creatDataSource(Node dataSourceNode) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Element dataSourceEle = (Element) dataSourceNode;
        NodeList propertyList = dataSourceEle.getElementsByTagName("property");
        int length = propertyList.getLength();
        Element propertyEle;
        Class<IzumiDataSource> dataSourceClass = IzumiDataSource.class;
        Constructor<IzumiDataSource> constructor = dataSourceClass.getConstructor(null);
        constructor.setAccessible(true);
        IzumiDataSource dataSource = constructor.newInstance();
        Field propertyField;
        String name;
        String value;
        for (int i = 0; i < length; i++) {
            propertyEle = (Element) propertyList.item(i);
            name = propertyEle.getAttribute("name");
            value = propertyEle.getAttribute("value");
            propertyField = dataSourceClass.getDeclaredField(name);
            propertyField.setAccessible(true);
            propertyField.set(dataSource, value);
        }
        Method initialcConnectionList = dataSourceClass.getDeclaredMethod("initialcConnectionList");
        initialcConnectionList.setAccessible(true);
        initialcConnectionList.invoke(dataSource);
        return dataSource;
    }
}
