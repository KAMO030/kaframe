package com.kamo.context.env.impl;


import com.kamo.context.annotation.Autowired;
import com.kamo.context.env.Environment;
import com.kamo.context.env.PropertyParser;

public class PropertiesPropertyParser implements PropertyParser<PropertiesPropertySource> {
    @Autowired
    private Environment environment;

    @Override
    public void parse(PropertiesPropertySource propertySource) {

    }


}
