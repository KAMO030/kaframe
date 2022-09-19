package com.kamo.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件资源信息解析器
 */
public class FileSystemResourceHolder implements ResourceHolder {

    private final File file;

    private final String path;

    public FileSystemResourceHolder(File file) {
        this.path = file.getPath();
        this.file = file;
    }

    public FileSystemResourceHolder(String path) {
        this.file = new File(path);
        this.path = path;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    public final String getPath() {
        return this.path;
    }

}
