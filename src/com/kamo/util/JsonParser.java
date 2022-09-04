package com.kamo.util;

import java.lang.reflect.*;
import java.util.*;

public class JsonParser {
    public static String object2JSON(Object object) {
        Class<?> type = object.getClass();
        String json = parseTypeStrategy(type,object);
        if (json == null) {
            json = do2JSONParser(type,object);
        }
        return json;

    }
    private static String parseTypeStrategy(Class type,Object object){
        if (type.isArray()) {
            return array2JSON(object);
        } else if (ReflectUtil.isCollection(type)) {
            return collection2JSON((Collection) object);
        } else if (ReflectUtil.isMap(type)) {
            return map2JSON((Map) object);
        } else if (ReflectUtil.isPrimitive(type)) {
            return object instanceof String ?
                    "\"" + object + "\""
                    : object.toString();
        }
        return null;
    }
    private static String do2JSONParser(Class type, Object object){
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        ReflectUtil.forEachField(type, field -> {
            if (Modifier.isTransient(field.getModifiers())) {
                return false;
            }
            Object fieldValue = ReflectUtil.getFieldValue(field, object);
            String fieldName = field.getName();
            if (fieldValue == null) {
                return false;
            }
            if (!ReflectUtil.isPrimitive(fieldValue.getClass())) {
                fieldValue = object2JSON(fieldValue);
            } else if (fieldValue instanceof String) {
                fieldValue = "\"" + fieldValue + "\"";
            }
            joiner.add("\"" + fieldName + "\":" + fieldValue);
            return false;
        });
        return joiner.toString();
    }
    private static String map2JSON(Map mapBean) {
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        for (Object key : mapBean.keySet()) {
            joiner.add(key.toString() + ":" + object2JSON(mapBean.get(key)));
        }
        return joiner.toString();
    }

    private static String collection2JSON(Collection collectionBean) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        collectionBean.forEach(o -> joiner.add(object2JSON(o)));
        return joiner.toString();
    }

    private static String array2JSON(Object array) {
        Class<?> beanClass = array.getClass();
        if (!beanClass.isArray()) {
            throw new IllegalArgumentException("[" + beanClass + "] 不是数组类型");
        }
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        ReflectUtil.forEachArray(array, a -> joiner.add(object2JSON(a)));
        return joiner.toString();
    }

    public static <T> T json2Object(String json, Class<T> type, Class... genericsType) {
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            Map<String, Object> keyValueMap = getKeyValueMap(json);
            return setPropertiesByMap(keyValueMap, type);
        } else if (json.startsWith("[") && json.endsWith("]")) {
            List<Object> valueList = getValueList(json);
            if (type.isArray()) {
                return (T) ReflectUtil.collection2Array(setPropertiesByList(valueList, type.getComponentType()));
            } else if (genericsType != null && genericsType.length >= 1 && ReflectUtil.isCollection(type)) {
                return (T) setPropertiesByList(valueList, genericsType[0]);
            }
        }
        throw new IllegalArgumentException(json + " json格式或类型错误");
    }

    public static <T> List<T> json2List(String json, Class<T> type) {
        json = json.trim();
        if (json.startsWith("[") && json.endsWith("]")) {
            List<Object> valueList = getValueList(json);
            return setPropertiesByList(valueList, type);
        }
        throw new IllegalArgumentException(json + " json格式错误");
    }


    private static <T> T setPropertiesByMap(Map<String, Object> keyValueMap, Class<T> type) {
        T object = ReflectUtil.newInstance(type);
        for (String key : keyValueMap.keySet()) {
            Field field = ReflectUtil.getField(type, key, true);
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            String fieldName = field.getName();
            Object value = keyValueMap.get(fieldName);
            Type fieldType = field.getGenericType();
            if (value instanceof Map) {
                value = setPropertiesByMap((Map<String, Object>) value, (Class<T>) fieldType);
            } else if (!(fieldType instanceof ParameterizedType) && ((Class) fieldType).isArray()) {
                Class componentType = ((Class) fieldType).getComponentType();
                List values = setPropertiesByList((List) value, componentType);
                value = Array.newInstance(componentType, values.size());
                ReflectUtil.collection2Array(value, values);

            } else if (value instanceof List) {
                value = setPropertiesByList((List) value, ReflectUtil.getActualTypeArgument(fieldType));
            } else if (value instanceof String) {
                value = ReflectUtil.parseString((Class) fieldType, (String) value);
            } else {
                continue;
            }
            ReflectUtil.setFieldValue(field, object, value);
        }
        return object;
    }

    private static <T> List<T> setPropertiesByList(List<Object> valueString, Class<T> type) {
        List<T> valueList = new ArrayList<>();
        for (Object value : valueString) {
            if (value instanceof Map) {
                valueList.add(setPropertiesByMap((Map<String, Object>) value, type));
            } else if (value instanceof String) {
                valueList.add(ReflectUtil.parseString(type, (String) value));
            }
        }
        return valueList;
    }

    private static List<Object> getValueList(String json) {
        List<Object> valueList = new ArrayList<>();
        json = json.substring(1, json.length() - 1).trim();
        char[] chars = json.toCharArray();
        int index = json.indexOf(",");
        if (index == -1) {
            if (!json.equals("")) {
                valueList.add(json);
            }
            return valueList;
        }
        int startIndex = 0;
        for (int i = 0; i < chars.length && index != -1; i++) {
            if (i == index) {
                valueList.add(json.substring(startIndex, index).replaceAll("\"", "").trim());
                startIndex = index + 1;
                index = json.indexOf(',', index + 1);
                if (index == -1) {
                    valueList.add(json.substring(startIndex).replaceAll("\"", "").trim());
                    break;
                }
            } else if (chars[i] == '{') {
                int commaIndex = i + getIndex(chars[i], json.substring(i));
                valueList.add(getKeyValueMap(json.substring(i, commaIndex + 1).trim()));
                index = json.indexOf(',', commaIndex);
                i = index;
            } else if (chars[i] == '[') {
                int commaIndex = i + getIndex(chars[i], json.substring(i));
                valueList.add(getValueList(json.substring(i, commaIndex + 1).trim()));
                index = json.indexOf(',', commaIndex);
                i = index;
            }
        }

        return valueList;
    }

    private static Map<String, Object> getKeyValueMap(String json) {
        Map<String, Object> keyValueMap = new HashMap<>();
        json = json.substring(1, json.length() - 1).trim();
        int prefix = 0;
        int suffix = json.indexOf(':');
        while (suffix != -1) {
            char charAt = json.charAt(prefix);
            //{, name=123, cinfoDao={id=123}} ^解决这种情况
            String key = json.substring(prefix, suffix).trim();
            String valString = json.substring(suffix + 1);
            String valTrim = valString.trim();
            Character ch = valTrim.charAt(0);
            int spaceCount = 0;
            char[] chars = valString.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == ' ') {
                    spaceCount++;
                    continue;
                }
                break;
            }
            prefix = suffix + 1 + spaceCount + getIndex(ch, valString);
            Object value = getValue(ch, json.substring(suffix + 1, prefix + 1).trim());
            if (value instanceof String) {
                value = ((String) value).replaceFirst(",", "").replaceAll("\"", "").trim();
                prefix++;
            } else {
                prefix = json.indexOf(',', prefix) + 1;
            }
            key = key.replaceFirst(",", "").replaceAll("\"", "").trim();
            keyValueMap.put(key, value);
            suffix = json.indexOf(':', prefix);

        }
        return keyValueMap;
    }

    public static int getIndex(char ch, String json) {
        int index = 1;
        int spaceCount = 0;
        if (ch == '{' || ch == '[') {
            int count = 1;
            char back = (char) (ch + 2);
            char[] chars = json.toCharArray();
            for (; index < chars.length; index++) {
                if (chars[index - 1] == ' ') {
                    spaceCount++;
                    continue;
                }
                break;
            }
            for (; index < chars.length; index++) {
                if (chars[index] == ch) {
                    count++;
                } else if (chars[index] == back) {
                    count--;
                }
                if (count == 0) {
                    break;
                }
            }
        } else {
            int indexOf = json.indexOf(',');
            index = indexOf != -1 ? indexOf - 1 : json.length() - 1;
        }
        return index + spaceCount;
    }

    public static Object getValue(Character ch, String json) {
        if (ch.equals('{')) {
            return getKeyValueMap(json);
        } else if (ch.equals('[')) {
            return getValueList(json);
        } else {
            return json;
        }
    }
}
