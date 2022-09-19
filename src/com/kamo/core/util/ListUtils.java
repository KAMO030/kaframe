package com.kamo.core.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public final class ListUtils {
    private ListUtils(){
    }

    public static <T> List<T> deduplication(List<T> oldList){
        List<T> newList = new ArrayList<>(new HashSet<>(oldList));
        return newList;
    }
}
