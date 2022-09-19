package com.kamo.core.io.impl;


import com.kamo.core.io.ResourceHolder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

/**
 * Url资源解析器
 */
public class UrlResourceHolder implements ResourceHolder {

    private final URL url;

    public UrlResourceHolder(URL url) {
        Objects.requireNonNull(url, "URL must not be null");
        this.url = url;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection con = this.url.openConnection();
        try {
            return con.getInputStream();
        } catch (IOException ex) {
            // 如果该URL为网络资源
            // 可以通过HTTP的方式读取云服务的文件,我们也可以把配置文件放到 GitHub 或者Gitee上
            if (con instanceof HttpURLConnection){
                ((HttpURLConnection) con).disconnect();
            }
            throw ex;
        }
    }

}
