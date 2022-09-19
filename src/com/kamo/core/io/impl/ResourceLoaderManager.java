package com.kamo.core.io.impl;


import com.kamo.core.io.ResourceHolder;
import com.kamo.core.io.ResourceLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 默认的资源加载器
 */
public class ResourceLoaderManager implements ResourceLoader {

    
    private final List<ResourceLoader> resourceLoaders = new ArrayList();

    public ResourceLoaderManager() {
        resourceLoaders.add(new ClassPathResourceLoader());
        resourceLoaders.add(new UrlResourceLoader());
        resourceLoaders.add(new FileSystemResourceLoader());
    }

    @Override
    public ResourceHolder getResourceHolder(String location) {
        Objects.requireNonNull(location, "Location must not be null");
        ResourceLoader matchResourceLoader = getMatchResourceLoader(location);
        return matchResourceLoader.getResourceHolder(location);
    }

    @Override
    public boolean isMatchLoader(String location) {
        return resourceLoaders.stream().anyMatch(resourceLoader->resourceLoader.isMatchLoader(location));
    }

    private ResourceLoader getMatchResourceLoader(String location){
        ResourceLoader matchLoader = null;

//        matchLoader = resourceLoaders.stream()
//                .filter(resourceLoader -> resourceLoader.isMatchLoader(location))
//                .findFirst().get();
        //以下为上面注释的另一种写法
        for (ResourceLoader resourceLoader : resourceLoaders) {
            if (resourceLoader.isMatchLoader(location)) {
                matchLoader = resourceLoader;
                break;
            }
        }
        if (matchLoader == null && !resourceLoaders.isEmpty()) {
            matchLoader = resourceLoaders.get(0);
        }
        Objects.requireNonNull(matchLoader, "matchResourceLoader is not match");
        return matchLoader;
    }
}
