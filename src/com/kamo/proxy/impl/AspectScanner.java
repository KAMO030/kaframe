package com.kamo.proxy.impl;

import com.kamo.context.annotation.AbstractScanner;
import com.kamo.context.annotation.Scanner;
import com.kamo.proxy.AspectResolve;
import com.kamo.proxy.annotation.AspectJ;

public class AspectScanner extends AbstractScanner implements Scanner {


    @Override
    public void register(Class beanClass) {
        new AspectResolve(beanClass).parse();
    }

    @Override
    public boolean isRegisterClass(Class loaderClass) {
        return loaderClass.isAnnotationPresent(AspectJ.class);
    }
}
