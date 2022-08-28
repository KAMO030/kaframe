package com.kamo.util;






import java.lang.reflect.Field;

import java.util.Map;

public final class BeanUtils {

    private BeanUtils() {
    }

    /**
     * 根据Map中的key给相同属性字段名赋值
     * @param obj 需要自动赋值的对象
     * @param parameterMap Map<String, String[]>类型
     * @return 返回自动赋值后的对象
     */
    public static Object autoAllocation(Object obj , Map<String, String[]> parameterMap){

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            String name = field.getName();
            String[] values = parameterMap.get(name);
            field.setAccessible(true);
            if (values==null||values.length == 0||values[0].equals("")) {
                continue;
            }
            Class type = field.getType();
            Object value = ConverterRegistry.convert(values,type);
            try {
                field.set(obj,value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return obj;
    }


}
