package jmu.shijh.tinymall.common.util;

import com.alibaba.fastjson.JSON;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapBuilder {
     public static class MapBuildAction {
         private final Map<Object, Object> map;

         MapBuildAction() {
             map = new LinkedHashMap<>();
         }

         MapBuildAction(Map<?, ?> map) {
             this.map = new LinkedHashMap<>(map);
         }

         public MapBuildAction add(Object key, Object value) {
             map.put(key, value);
             return this;
         }

         @SuppressWarnings("unchecked")
         public <K,V> Map<K,V> build() {
             return (Map<K,V>) map;
         }

         public String json() {
             return JSON.toJSONString(this.map);
         }
     }

    public static MapBuildAction map() {
        return new MapBuildAction();
    }

    public static MapBuildAction map(Map<?, ?> map) {
        return new MapBuildAction(map);
    }
}
