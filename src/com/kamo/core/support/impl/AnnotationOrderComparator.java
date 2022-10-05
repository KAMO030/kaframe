package com.kamo.core.support.impl;

import com.kamo.core.annotation.Order;
import com.kamo.core.util.AnnotationUtils;

import java.util.List;
import java.util.Set;

public class AnnotationOrderComparator extends OrderComparator {
    public static final AnnotationOrderComparator INSTANCE = new AnnotationOrderComparator();

   public static void sort(List list){
       list.sort(INSTANCE);
   }

    @Override
    protected int getOrder(Object object) {
        return AnnotationUtils.getValue(object.getClass(), Order.class, 1);
    }
}