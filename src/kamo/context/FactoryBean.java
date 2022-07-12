package kamo.context;

public interface FactoryBean<T> {
   T getObject();
   Class<T> getObjectType();
   default boolean isSingleton(){
      return true;
   }
}
