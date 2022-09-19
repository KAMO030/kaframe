package com.kamo.core.io;


import com.kamo.core.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

/**
 * 默认的资源加载器
 */
public class UrlResourceLoader implements ResourceLoader {
    private static final String URL_URL_PREFIX = "url:";

    @Override
    public ResourceHolder getResourceHolder(String location) {
        Objects.requireNonNull(location, "Location must not be null");
        String urlLocation = StringUtils.subStringOnAfter(location, URL_URL_PREFIX);
        try {
            URL url = new URL(urlLocation);
            return new UrlResourceHolder(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isMatchLoader(String location) {
        return location.toLowerCase().startsWith(URL_URL_PREFIX);
    }

}
