package com.kamo.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface ClassParser {
   default Map parse(Class type){
       Map mappingMap = new ConcurrentHashMap<>();
       Field[] fields = type.getDeclaredFields();
       Method[] methods = type.getDeclaredMethods();
//       for (Field field : fields) {
//           if(){
//               doParserField();
//           }
//       }
       return mappingMap;
   };

}
