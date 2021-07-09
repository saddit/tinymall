package jmu.shijh.tinymall.common.util;

import lombok.NonNull;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Collections Factory
 *
 * @since 1.7
 */
public class Cl {

    @SafeVarargs
    public static <T> Collection<T> coll(@NonNull T... item) {
        return list(item);
    }

    public static <K,V> Map<K,V> single2Map(K key, V value) {
        return Collections.singletonMap(key,value);
    }

    public boolean empty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    //@SuppressWarnings("unchecked")
    //public static <T> List<T> union(List<T> list1, List<T> list2) {
    //
    //}

    @SafeVarargs
    public static <T> List<T> list(@NonNull T... item) {
        return Arrays.asList(item);
    }

    public static MapBuilder.MapBuildAction map() {
        return MapBuilder.map();
    }

    @SuppressWarnings(value = "unchecked")
    public static<T> T[] emptyArr(Class<T> clazz) {
        Object arr = Array.newInstance(clazz, 0);
        return (T[]) arr;
    }
}
