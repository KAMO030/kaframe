package com.kamo.context.annotation;

import java.io.File;
import java.util.Arrays;
import java.util.Queue;

public abstract class AbstractScanner implements Scanner {
    private String rootPath;
    private ClassLoader classLoader;



    public AbstractScanner() {
        classLoader = Thread.currentThread().getContextClassLoader();
    }

    public void scan(String[] basePackages) {
        for (String basePackage : basePackages) {
            int index = basePackage.indexOf('/');
            basePackage = basePackage.replace(".", "/");
            //test.dao
            if (basePackage.startsWith("classpath:")) {
                index = index != -1 ? index : basePackage.length();
                rootPath = basePackage.substring(10, index);
            }else {
                rootPath = index != -1 ? basePackage.substring(0, index) : basePackage;
            }
            String filePath = classLoader.getResource(basePackage).getFile();
            File classFile = new File(filePath);
            doScan(classFile);
        }
    }
    protected void doScan(File classFile) {
        Class beanClass;
        if (!classFile.isDirectory()){
            throw new RuntimeException();
        }
        File[] files = classFile.listFiles();
        for (File file : files) {
            beanClass = loaderClass(file);
            if (beanClass == null) {
                continue;
            }
            if (isRegisterClass(beanClass)) {
                register(beanClass);
            }
        }

    }
    protected Class loaderClass(File classFile) {
        if (classFile.isDirectory()) {
            doScan(classFile);
        } else if (classFile.isFile()) {
            if (classFile.getName().endsWith(".class")) {
//                test/A
                String className = classFile.getAbsolutePath();
                String start = rootPath.replace('/', '\\');
                className = className.substring(
                                className.lastIndexOf(start),
                                className.indexOf(".class"))
                        .replace("\\", ".");
                Class beanClass = null;
                try {
                    beanClass = classLoader.loadClass(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return beanClass;
            }
        }
        return null;
    }
}
