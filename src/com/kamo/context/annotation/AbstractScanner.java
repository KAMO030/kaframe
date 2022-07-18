package com.kamo.context.annotation;

import java.io.File;

public abstract class AbstractScanner implements Scanner {
    private String rootPath;
    public void scan(String[] basePackages) {
        for (String basePackage : basePackages) {
            basePackage = basePackage.replace(".", "/");
            int index = basePackage.indexOf('/');
            //test.dao
            rootPath = index != -1 ? basePackage.substring(0, index) : basePackage;
            String filePath = AnnotationConfigApplicationContext.class.getClassLoader().getResource(basePackage).getFile();
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
                className = className.substring(
                                className.lastIndexOf(rootPath),
                                className.indexOf(".class"))
                        .replace("\\", ".");
                Class beanClass = null;
                try {
                    beanClass = AnnotationConfigApplicationContext.class.getClassLoader().loadClass(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return beanClass;
            }
        }
        return null;
    }
}
