package kamo.proxy.impl;

import kamo.context.annotation.AbstractScanner;
import kamo.context.annotation.Scanner;
import kamo.proxy.AspectResolve;
import kamo.proxy.annotation.AspectJ;

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
