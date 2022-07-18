package com.kamo.context;

public interface FactoryBean<T> {
   default T getObject(){
      return null;
   };
   Class<T> getObjectType();
   default boolean isSingleton(){
      return true;
   }

  default T getObject(Class<T> type){
     return getObject();
  };
}
