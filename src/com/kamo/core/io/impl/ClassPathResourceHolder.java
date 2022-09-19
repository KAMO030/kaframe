package com.kamo.core.io.impl;


import com.kamo.core.io.ResourceHolder;
import com.kamo.core.util.ClassUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * 类路径资源解析器
 */
public class ClassPathResourceHolder implements ResourceHolder {

    private final String path;

    private ClassLoader classLoader;

    public ClassPathResourceHolder(String path) {
        this(path, null);
    }

    public ClassPathResourceHolder(String path, ClassLoader classLoader) {
        Objects.requireNonNull(path, "Path must not be null");
        this.path = path;
        this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        // 通过类路径获取文件输入流
        InputStream is = classLoader.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException(this.path + " cannot be opened because it does not exist");
        }
        return is;
    }

}
