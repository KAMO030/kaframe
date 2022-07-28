package com.kamo.context_rpc;

import com.kamo.context.annotation.AbstractScanner;
import com.kamo.context.annotation.Service;

public class ReferenceScanner extends AbstractScanner {

    @Override
    public void register(Class beanClass) {

    }

    @Override
    public boolean isRegisterClass(Class loaderClass) {
        return loaderClass.isInterface()&&loaderClass.isAnnotationPresent(Service.class);
    }
}
