package com.kamo.core.util;

import com.kamo.core.support.impl.AnnotationOrderComparator;

import java.util.*;

public final class ListUtils {
    private ListUtils(){
    }

    public static <T> List<T> deduplication(List<T> oldList){
        List<T> newList = new ArrayList<>(new HashSet<>(oldList));
        return newList;
    }

    public static <T> List<T> array2List(T[] array) {
        List<T> list = new ArrayList<>(Arrays.asList(array));
        return list;
    }
    public static <T> void sort(List<T> list, Comparator<? super T> c) {
        list.sort(c);
    }
    public static <T> void sort(List<T> list) {
        AnnotationOrderComparator.sort(list);
    }
}
