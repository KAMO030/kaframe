package com.kamo.util;


import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public abstract class BeanUtil {

    public static String toBeanName(String columnName) {
        columnName = columnName.toLowerCase();
        if (columnName.indexOf("_") != -1) {
            columnName = columnName.toLowerCase();
            String[] strings = columnName.split("_");
            String beanName = strings[0];
            for (int i = 1; i < strings.length; i++) {
                char[] chars = strings[i].toCharArray();
                chars[0] = Character.toUpperCase(chars[0]);
                beanName += new String(chars);
            }
            return beanName;
        }
        return columnName;
    }



    public static boolean isExtends(Class clazz, Class supClass) {
        boolean bool = false;
        if (clazz != Object.class) {
            do {
                if (clazz == supClass) {
                    bool = true;
                    break;
                }
            } while (clazz != null && (clazz = clazz.getSuperclass()) != Object.class);
        }
        return bool;
    }

    public static boolean isExtends(String className, Class supClass) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        return isExtends(clazz, supClass);
    }

    public static String getMD5Hash(String msg) {
        byte[] b = null;
        String md5Code = null;
        try {
            b = MessageDigest.getInstance("md5").digest(msg.getBytes());
            md5Code = new BigInteger(1, b).toString(16);
            for (int i = 0; i < 32 - md5Code.length(); i++) {
                md5Code = "0" + md5Code;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5Code;
    }


    public static void assignByBean(String beanData, Object bean, Class beanType) {
        String[] fieldPairs = beanData.split("&");
        for (String fieldPair : fieldPairs) {
            String[] field = fieldPair.split("=");
            String fieldName = field[0];
            String fieldValue = "";
            if (field.length != 1) {
                fieldValue = field[1];
            }
            Field ff = ReflectUtil.getField(beanType, fieldName);
            ff.setAccessible(true);
            ReflectUtil.setFieldValue(ff, bean, ReflectUtil.parseString(ff.getType(), fieldValue));
        }
    }





    public static String toTableName(String name) {
        char[] chars = name.toCharArray();
        StringBuffer buffer = new StringBuffer();
        buffer.append(chars[0]);
        for (int i = 1; i < chars.length; i++) {
            if (Character.isUpperCase(chars[i])) {
                buffer.append("_");
            }
            buffer.append(chars[i]);
        }
        return buffer.toString().toUpperCase();
    }

    public static String autoStitchingSql(Object entity, String refer, List args) {
        String stitchingSql = "";
        if (entity == null) {
            return "";
        }
        Class<?> entityClass = entity.getClass();
        Field[] entityFields = entityClass.getDeclaredFields();
        try {
            for (Field entityField : entityFields) {
                Object value = null;
                String columnName = entityField.getName();
                entityField.setAccessible(true);
                value = entityField.get(entity);
                if (value != null) {
                    stitchingSql += refer.replace("$", columnName);
                    if (refer.indexOf("like") != -1) {
                        value = "%" + value + "%";
                    }
                    args.add(value);
                }
            }
            return stitchingSql;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }



//    public static Map<String, Object> getColumnMap(List<String>columnName,Object voBean)  {
//        return getColumnMap(columnName.toArray(new String[0]),voBean);
//    }
//    public static Map<String, Object> getColumnMap(String columnName,Object voBean)  {
//        return getColumnMap(columnName.split(","),voBean);
//    }
//    public static Object getColumn(String columnName,Object voBean)  {
//
//        columnName = columnName.trim();
//        try {
//            return getColumnMapVal(voBean.getClass().getDeclaredFields(),columnName,voBean);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//    public static Map<String, Object> getColumnMap(String[] columnName,Object voBean) {
//        Map<String, Object> columnMap = new HashMap<>();
//        Class vosClass = voBean.getClass();
//        Field[] voFields = vosClass.getDeclaredFields();
//        Object val;
//       try {
//           for (String name : columnName){
//               name = name.trim();
//               if (name.equals("")){
//                   continue;
//               }
//               val = getColumnMapVal(voFields,name,voBean);
//               columnMap.put(name,val);
//           }
//       } catch (IllegalAccessException e) {
//           e.printStackTrace();
//       }
//        return columnMap;
//    }
//    private static Object getColumnMapVal(Field[] fields,String name,Object voBean) throws IllegalAccessException {
//        Object val = null;
//        for (Field voField : fields) {
//            Class beanClass = voField.getType();
//            voField.setAccessible(true);
//            if (voField.getName().equals(name)){
//                return voField.get(voBean);
//            } else if (isExtends(beanClass, BaseBeanAbs.class)) {
//                try {
//                    Field columnField = beanClass.getDeclaredField(name);
//                    columnField.setAccessible(true);
//                    Object fieldVal = voField.get(voBean);
//                    val = columnField.get(fieldVal);
//                    break;
//                }catch (NoSuchFieldException e) {
//                }
//            }else {
//                val =  getColumnMapVal(beanClass.getDeclaredFields(),name,voField.get(voBean));
//                if (val!=null){
//                    return val;
//                }
//            }
//        }
//        return val;
//    }

}
