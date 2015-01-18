package com.uttt.common.utils;

import java.lang.reflect.Array;
import java.util.List;

public class ArrayUtils {
	
	public static <E> int getNestedListMaxSize(final List<List<E>> list) {
	    if (list == null) {
	        return 0;
	    }
	    int result = 0;
	    for (final List<E> innerList: list) {
	        if (innerList == null) {
	            continue;
	        }
	        result = Math.max(result, innerList.size());
	    }
	    return result;
	}
	
	@SuppressWarnings("rawtypes")
	public static <E> Class getNestedListClass(final List<List<E>> list) {
	    if (list == null) {
	        return null;
	    }

	    for (final List<E> innerList: list) {
	        if (innerList == null || innerList.isEmpty()) {
	            continue;
	        }
	        return innerList.get(0).getClass();
	    }
	    return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <E> E[][] convertNestedList(final List<List<E>> list) {
	    final Class<?> clazz = getNestedListClass(list);
	    if (clazz == null) {
	        return null;
	    }
	    final E[][] result = (E[][]) Array.newInstance(clazz, list.size(), 
	        getNestedListMaxSize(list));

	    for (int i = 0; i < list.size(); i++) {
	        final List<E> innerList = list.get(i);
	        if (innerList == null) {
	            continue;
	        }
	        final E[] innerListArray = (E[]) Array.newInstance(clazz, innerList.size());
	        result[i] = innerList.toArray(innerListArray);
	    }
	    return result;
	}
}
