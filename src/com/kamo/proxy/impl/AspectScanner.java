package com.kamo.proxy.impl;

import com.kamo.context.annotation.Scanner;
import com.kamo.context.annotation.support.AbstractScanner;
import com.kamo.proxy.AspectResolve;
import com.kamo.proxy.annotation.Aspect;

public class AspectScanner extends AbstractScanner implements Scanner {


    public AspectScanner() {
    }

    @Override
    public void register(Class beanClass) {
        new AspectResolve(beanClass).parse();
    }

    @Override
    public boolean isRegisterClass(Class loaderClass) {
        return loaderClass.isAnnotationPresent(Aspect.class);
    }
}
