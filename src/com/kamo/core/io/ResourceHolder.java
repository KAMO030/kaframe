package com.kamo.core.io;

import java.io.*;

/**
 * 资源加载接口
 */
public interface ResourceHolder {

    InputStream getInputStream() throws IOException;

    default Reader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}
