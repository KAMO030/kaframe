package com.kamo.context.env;



public interface Environment extends PropertyParser,PropertyHolder{


    void registerPropertyParser(PropertyParser propertyParser);
}