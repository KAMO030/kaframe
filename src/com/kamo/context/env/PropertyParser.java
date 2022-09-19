package com.kamo.context.env;


import com.kamo.core.util.ReflectUtils;

public interface PropertyParser<T extends PropertySource>  {
    void parse(T propertySource);

   default Class  propertySourceType(){
       Class parserType = this.getClass();
       Class propertySourceType = ReflectUtils.getActualTypeOnInterface(parserType,PropertyParser.class.getName());
       if (propertySourceType.equals(PropertySource.class)){
           throw new IllegalArgumentException(parserType.getName()+" : 没有指定数据源泛型");
       }
       return propertySourceType;
   }
}
