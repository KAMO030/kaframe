package com.kamo.context.factory;

public interface Aware{
    void setAware(Object... awareValue);
    Class[] getAwareTypes();

    default String[] getAwareNames(){
        Class[] awareTypes = getAwareTypes();
        String[] awareNames = new String[awareTypes.length];
        for (int i = 0; i < awareTypes.length; i++) {
            awareNames[i] = awareTypes[i].getSimpleName();
        }
        return awareNames;
    };

    default Object[] getAwareValues( BeanFactory context){
        String[] awareNames = getAwareNames();
        Class[] awareTypes = getAwareTypes();
        Object[] awareValues = new Object[awareTypes.length];
        for (int i = 0; i < awareNames.length; i++) {
            awareValues[i] = context.getBean(awareNames[i], awareTypes[i]);
        }
        return awareValues;
    }

}
