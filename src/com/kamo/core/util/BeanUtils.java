package com.kamo.core.util;


import java.lang.reflect.Field;
import java.util.List;


public abstract class BeanUtils {

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
