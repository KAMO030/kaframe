package kamo.factory;


import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassPathXmlApplicationContext implements ApplicationContext {
    private String path;
    private Map<String, Beans> beanMap;
    private  List<BeanPostProcessor> postProcessors;
    private ClassLoader cl = getClass().getClassLoader();

    public ClassPathXmlApplicationContext(String path) {
        this.path = path.trim();
        beanMap = new ConcurrentHashMap<>();
        postProcessors = new ArrayList<>();
        loadBeans(getClass().getClassLoader().getResourceAsStream(path));
        for (Beans bean:beanMap.values()){
            bean.setObj(buildObject(bean));
        }
    }
    public ClassPathXmlApplicationContext(InputStream stream) {
        beanMap = new ConcurrentHashMap<>();
        postProcessors = new ArrayList<>();
        loadBeans(stream);
        for (Beans bean:beanMap.values()){
            bean.setObj(buildObject(bean));
        }
    }
    public ClassPathXmlApplicationContext(InputStream stream,ClassLoader classLoader) {
        cl = classLoader;
        beanMap = new ConcurrentHashMap<>();
        postProcessors = new ArrayList<>();
        loadBeans(stream);
        for (Beans bean:beanMap.values()){
            bean.setObj(buildObject(bean));
        }
    }
    private void loadBeans(InputStream stream) {

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(stream);
            NodeList nodeList = doc.getElementsByTagName("bean");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node beanNode = nodeList.item(i);
                if (beanNode instanceof Element) {
                    Element beanElement = (Element) beanNode;
                    Beans bean = (Beans) getAttribute(beanElement, Beans.class);
                    if (beanElement.hasChildNodes()) {
                        NodeList propertyNode = beanElement.getElementsByTagName("property");
                        NodeList constructorNode = beanElement.getElementsByTagName("constructor-arg");
                        if (propertyNode.getLength() > 0) {
                            bean.setParameters((ArrayList<Property>) setBeanPar(propertyNode, Property.class));
                        } else if (constructorNode.getLength() > 0) {
                            bean.setParameters((ArrayList<Property>) setBeanPar(constructorNode, Constructor_Arg.class));
                        }
                    }
                    beanMap.put(bean.getId(), bean);
                }

            }
            creatAOP(doc);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
    private void creatAOP(Document doc) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        NodeList aopList = doc.getElementsByTagName("aop");
        int length = aopList.getLength();
        Class aspectClass;
        Class pointCutClass;
        AspectJ aspectJ;
        PointCut pointCut;
        Beans beans;
        Object target;
        for (int i = 0; i < length; i++) {
            Element aopElement = (Element) aopList.item(i);
            aspectClass = Class.forName(aopElement.getAttribute("aspect"));
            pointCutClass = Class.forName(aopElement.getAttribute("pointCut"));
            aspectJ = (AspectJ) aspectClass.newInstance();
            pointCut = (PointCut) pointCutClass.newInstance();
            pointCut.setAspectJ(aspectJ);
            beans = beanMap.get(aopElement.getAttribute("target"));
            target = buildObject(beans);
            pointCut.setTarget(target);
            Object proxy = Proxy.newProxyInstance(getClass().getClassLoader(), buildObject(beans).getClass().getInterfaces(), pointCut);
            beans.setObj(proxy);
        }

    }
    private List setBeanPar(NodeList nodeList, Class aclass) throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List list = new ArrayList();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Element element = (Element) node;
                list.add(getAttribute(element, aclass));
            }
        }
        return list;
    }

    private Object getAttribute(Element element, Class aclass) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Object o = aclass.getDeclaredConstructor().newInstance();
        NamedNodeMap elementAttributes = element.getAttributes();
        for (int i1 = 0; i1 < elementAttributes.getLength(); i1++) {
            Class temp = aclass;
            String name = elementAttributes.item(i1).getNodeName().trim();
            if (o instanceof Beans) {
                if (name.equals("class")) {
                    name += "Name";
                } else if (name.equals("factory-bean")) {
                    name = "factoryId";
                } else if (name.equals("factory-method")) {
                    name = "factoryMethodName";
                }
                else if (name.equals("factory-method-par")) {
                    name = "factoryMethodPar";
                }
                else if (name.equals("factory-method-parTypes")) {
                    name = "factoryMethodParTypes";
                }
            }
            Field field = temp.getDeclaredField(name);
            if (field == null) {
                continue;
            }
            field.setAccessible(true);
            if (name.equals("factory-method-parTypes")){
                field.set(o, elementAttributes.item(i1).getNodeValue().split(","));
            }else {
                field.set(o, elementAttributes.item(i1).getNodeValue());
            }
        }
        if (!(element.hasAttribute("value") || element.hasAttribute("ref")) && !(o instanceof Beans)) {
            Property p = null;
            Constructor_Arg c = null;
            if (o instanceof Property) {
                p = (Property) o;
            } else if (o instanceof Constructor_Arg) {
                c = (Constructor_Arg) o;
            }
            NodeList valueNodes = element.getElementsByTagName("value");
            NodeList arrayNodes = element.getElementsByTagName("array");
            NodeList listNodes = element.getElementsByTagName("list");
            Node item;
            if (listNodes.getLength() > 0) {
                item = listNodes.item(0);
                if (o instanceof Property) {
                    p.setType(p.getType() + "_list");
                } else if (o instanceof Constructor_Arg) {
                    c.setType(c.getType() + "_list");
                }
                Element e = (Element) item;
                NodeList childNodes = e.getElementsByTagName("value");
                String[] values = new String[childNodes.getLength()];
                for (int i = 0; i < childNodes.getLength(); i++) {
                    values[i] = childNodes.item(i).getTextContent();
                }
                if (o instanceof Property) {
                    p.setValues(values);
                } else if (o instanceof Constructor_Arg) {
                    c.setValues(values);
                }
            } else if (arrayNodes.getLength() > 0) {
                item = arrayNodes.item(0);
                if (o instanceof Property) {
                    p.setType(p.getType() + "_array");
                } else if (o instanceof Constructor_Arg) {
                    c.setType(c.getType() + "_array");

                }
                Element e = (Element) item;
                NodeList childNodes = e.getElementsByTagName("value");
                String[] values = new String[childNodes.getLength()];
                for (int i = 0; i < childNodes.getLength(); i++) {
                    values[i] = childNodes.item(i).getTextContent();
                }
                if (o instanceof Property) {
                    p.setValues(values);
                } else if (o instanceof Constructor_Arg) {
                    c.setValues(values);
                }

            } else if (valueNodes.getLength() > 0) {
                String value = valueNodes.item(0).getTextContent();
                if (o instanceof Property) {
                    p.setValue(value);
                } else if (o instanceof Constructor_Arg) {
                    c.setValue(value);
                }
            }
        }
        return o;
    }


    private Object buildObject(Beans bean) {
        Object o = bean.getObj();
        Class<?> objClass = null;
        if (o!=null){
            return o;
        }
        try {
            if (bean.getFactoryId()!=null&&bean.getFactoryMethodName()!=null){
                return buildObjectByfactory(bean);
            }
            objClass = cl.loadClass(bean.getClassName());
            if (Modifier.isAbstract(objClass.getModifiers())) {
                return o;
            }
            List parameters = bean.getParameters();
            if (parameters!=null ) {
                if (parameters.get(0) instanceof Constructor_Arg) {
                    List<Constructor_Arg> constructorArgs = parameters;
                    Constructor[] declaredConstructors = objClass.getDeclaredConstructors();
                    Constructor defaultConstructor = null;
                    Class[] conType = getConType(constructorArgs);
                    boolean flag = false;
                    for (Constructor cc : declaredConstructors) {
                        for (int i = 0; i < cc.getParameterCount() && cc.getParameterCount() <= conType.length; i++) {
                            if (!(cc.getParameterTypes()[i] == conType[i])) {
                                flag = false;
                            } else {
                                flag = true;
                            }
                        }
                        if (flag && cc.getParameterCount() == conType.length) {
                            defaultConstructor = cc;
                            break;
                        }
                    }
                    if (defaultConstructor == null) {
                        throw new RuntimeException();
                    }
                    Object[] objects = new Object[defaultConstructor.getParameterCount()];
                    Parameter[] ps = defaultConstructor.getParameters();
                    for (Constructor_Arg cA : constructorArgs) {
                        String index = cA.getIndex();
                        Class type = ps[Integer.valueOf(index)].getType();
                        String name = cA.getName();
                        String va = cA.getValue();
                        String[] vas = cA.getValues();
                        Object value = null;
                        if (type.isArray()) {
                            value = getArray(type, vas);
                        } else if (cA.getType() != null && cA.getType().lastIndexOf("list") != -1) {
                            value = getList(cA.getType(), vas);
                        } else {
                            value = getObjType(va, type);
                        }
                        if (index != null) {
                            objects[Integer.valueOf(index)] = value;
                        }
                    }
                    o = defaultConstructor.newInstance(objects);
                } else if(parameters.get(0) instanceof Property){
                    Constructor<?> declaredConstructor = objClass.getDeclaredConstructor();
                    declaredConstructor.setAccessible(true);
                    o = declaredConstructor.newInstance();
                    List<Property> properties = parameters;
                    for (Property pp : properties) {
                        String va = (String) pp.getValue();
                        String name = pp.getName();
                        String ref = pp.getRef();
                        Field field = objClass.getDeclaredField(pp.getName());
                        Class type = field.getType();
                        String[] vas = pp.getValues();
                        field.setAccessible(true);
                        if (va != null) {
                            field.set(o, getObjType(va, type));
                        } else if (ref != null) {
                            field.set(o, getBean(ref));
                        } else if (pp.getType() != null && pp.getType().lastIndexOf("list") != -1) {
                            String t = pp.getType();
                            field.set(o, getList(t, vas));
                        } else if (type.isArray()) {
                            field.set(o, getArray(type, vas));
                        }
                    }
                }
            }else {
                o = objClass.newInstance();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        for (BeanPostProcessor beanPostProcessor:postProcessors){
            o = beanPostProcessor.postProcessBeforInitialization(bean.getId(),o);
        }
        for (BeanPostProcessor beanPostProcessor:postProcessors){
             o = beanPostProcessor.postProcessAfterInitialization(bean.getId(),o);
        }

        if(o instanceof BeanPostProcessor){
            postProcessors.add((BeanPostProcessor) o);
            return null;
        }
        return o;
    }

    private Object buildObjectByfactory(Beans bean) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String factoryMethodPar = bean.getFactoryMethodPar();
        String factoryMethodName = bean.getFactoryMethodName();
        String[] factoryParTypes = bean.getFactoryMethodParTypes();
        Object factorBean = buildObject(beanMap.get(bean.getFactoryId()));
        Class<?> factoryClass = factorBean.getClass();
        Method factoryMethod ;
        if (factoryMethodPar ==null){
            factoryMethod = factoryClass.getDeclaredMethod(factoryMethodName);
            factoryMethod.setAccessible(true);
            return factoryMethod.invoke(factorBean);
        }else {
            factoryMethod = factoryClass.getDeclaredMethod(factoryMethodName,String.class);
            factoryMethod.setAccessible(true);
            return factoryMethod.invoke(factorBean,factoryMethodPar);
        }
    }

    private List getList(String type, String[] vas) {
        type = type.split("_")[0];
        List list = new ArrayList();
        for (int i = 0; i < vas.length; i++) {
            list.add(getObjType(vas[i], type));
        }
        return list;

    }

    private Object getArray(Class type, String[] vas) {
        Class c = null;
        Object array = null;
        if (type == String[].class) {
            c = String.class;
            array = vas;
        } else if (type == int[].class) {
            c = int.class;
            array = Array.newInstance(c, vas.length);
        } else if (type == float[].class) {
            c = float.class;
            array = Array.newInstance(c, vas.length);
        } else if (type == long[].class) {
            c = long.class;
            array = Array.newInstance(c, vas.length);
        } else if (type == double[].class) {
            c = double.class;
            array = Array.newInstance(c, vas.length);
        } else if (type == boolean[].class) {
            c = boolean.class;
            array = Array.newInstance(c, vas.length);
        } else if (type == char[].class) {
            c = char.class;
            array = Array.newInstance(c, vas.length);
        } else if (type == byte[].class) {
            c = byte.class;
            array = Array.newInstance(c, vas.length);
        }
        for (int i = 0; i < vas.length; i++) {
            Array.set(array, i, getObjType(vas[i], c));
        }
        return array;
    }

    private Object getObjType(String va, Class type) {
        Object value = null;
        if (va.equals("")) {

        } else if (type == String.class) {
            value = String.valueOf(va);
        } else if (type == int.class) {
            value = Integer.valueOf(va);
        } else if (type == float.class) {
            value = Float.valueOf(va);
        } else if (type == long.class) {
            value = Long.valueOf(va);
        } else if (type == double.class) {
            value = Double.valueOf(va);
        } else if (type == char.class) {
            value = va.charAt(0);
        } else if (type == boolean.class) {
            value = Boolean.valueOf(va);
        } else if (type == short.class) {
            value = Short.valueOf(va);
        } else if (type == byte.class) {
            value = Byte.valueOf(va);
        }
        return value;
    }

    private Object getObjType(String va, String type) {
        Object value = null;
        if (va.equals("")) {

        } else if (type == null || type.toLowerCase().equals("string")) {
            value = String.valueOf(va);
        } else if (type.toLowerCase().equals("int")) {
            value = Integer.valueOf(va);
        } else if (type.toLowerCase().equals("float")) {
            value = Float.valueOf(va);
        } else if (type.toLowerCase().equals("long")) {
            value = Long.valueOf(va);
        } else if (type.toLowerCase().equals("double")) {
            value = Double.valueOf(va);
        } else if (type.toLowerCase().equals("char")) {
            value = va.charAt(0);
        } else if (type.toLowerCase().equals("boolean")) {
            value = Boolean.valueOf(va);
        } else if (type.toLowerCase().equals("short")) {
            value = Short.valueOf(va);
        } else if (type.toLowerCase().equals("byte")) {
            value = Byte.valueOf(va);
        }else if (type.toLowerCase().equals("ref")) {
            value = getBean(va);
        }
        return value;
    }

    private Class[] getConType(List<Constructor_Arg> constructorArgs) {
        Class[] ConType = new Class[constructorArgs.size()];
        for (Constructor_Arg cA : constructorArgs) {
            String type = cA.getType();
            int index = Integer.valueOf(cA.getIndex());
            if (type == null || type.toLowerCase().equals("string")) {
                ConType[index] = String.class;
            } else if (type.toLowerCase().equals("int")) {
                ConType[index] = int.class;
            } else if (type.toLowerCase().equals("float")) {
                ConType[index] = float.class;
            } else if (type.toLowerCase().equals("long")) {
                ConType[index] = long.class;
            } else if (type.toLowerCase().equals("double")) {
                ConType[index] = double.class;
            } else if (type.toLowerCase().equals("char")) {
                ConType[index] = char.class;
            } else if (type.toLowerCase().equals("boolean")) {
                ConType[index] = boolean.class;
            } else if (type.toLowerCase().equals("short")) {
                ConType[index] = short.class;
            } else if (type.toLowerCase().equals("byte")) {
                ConType[index] = byte.class;
            } else if (type.toLowerCase().equals("int_array")) {
                ConType[index] = int[].class;
            } else if (type.toLowerCase().equals("float_array")) {
                ConType[index] = float[].class;
            } else if (type.toLowerCase().equals("long_array")) {
                ConType[index] = long[].class;
            } else if (type.toLowerCase().equals("double_array")) {
                ConType[index] = double[].class;
            } else if (type.toLowerCase().equals("char_array")) {
                ConType[index] = char[].class;
            } else if (type.toLowerCase().equals("boolean_array")) {
                ConType[index] = boolean[].class;
            } else if (type.toLowerCase().equals("short_array")) {
                ConType[index] = short[].class;
            } else if (type.toLowerCase().equals("byte_array")) {
                ConType[index] = byte[].class;
            }else if (type.lastIndexOf("list")!=-1) {
                ConType[index] = List.class;
            }

        }


        return ConType;
    }

    @Override
    public boolean containsBean(String name) {
        return beanMap.containsKey(name);
    }

    @Override
    public String[] getAliases(String name) {
        return new String[0];
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) {
        return null;
    }

    @Override
    public Object getBean(String name) {
        Beans bean = beanMap.get(name);
        Object obj = bean.getObj();
        if (obj == null) {
            obj = buildObject(bean);
            bean.setObj(obj);
        }
        return obj;
    }


    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return null;
    }

    @Override
    public Object getBean(String name, Object... args) {
        return null;
    }

    @Override
    public Class<?> getType(String name) {
        return null;
    }

    @Override
    public Class<?> getType(String name, boolean allowFactoryBeanInit) {
        return null;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> typeToMatch) {
        return false;
    }

    @Override
    public Object[] getBeans() {
        Object[] beans = new Object[beanMap.size()];
        Collection<Beans> values = beanMap.values();
        int i=0;
        for (Beans value : values) {
            beans[i]=value.getObj();
            i++;
        }
        return beans;
    }
}
