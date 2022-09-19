package com.kamo.context_rpc;

import com.kamo.bean.annotation.Service;
import com.kamo.context.annotation.support.AbstractScanner;

public class ReferenceScanner extends AbstractScanner {

    public ReferenceScanner() {
    }

    @Override
    public void register(Class beanClass) {

    }

    @Override
    public boolean isRegisterClass(Class loaderClass) {
        return loaderClass.isInterface()&&loaderClass.isAnnotationPresent(Service.class);
    }
}
