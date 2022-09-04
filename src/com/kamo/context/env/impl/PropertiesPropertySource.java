package com.kamo.context.env.impl;

import com.kamo.context.env.PropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

public class PropertiesPropertySource extends PropertySource<Properties>{

    public Object setProperty(String key, String value) {
        return source.setProperty(key, value);
    }

    public void load(Reader reader) throws IOException {
        source.load(reader);
    }

    public void load(InputStream inStream) throws IOException {
        source.load(inStream);
    }
}
