package com.kuang2010.googleplay20.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wangkun1_kzx on 2018/3/21.
 * Gson 的工具类
 */

public class GsonUtil {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    /**
     * 将 object 转换成 json
     *
     * @param object
     * @return
     */
    public static String object2String(Object object) {
        return gson.toJson(object);
    }

    /**
     * 将 json字符串转换成 javabean
     * Bean解析
     * @param jsonString {"":"","":{},"":[]}
     * @param cls
     * @return
     */
    public static <T> T json2Bean(String jsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(jsonString, cls);
        }
        return t;
    }

    /**
     * 将json字符串转换成List
     * 泛型解析
     * @param json [{},{}]
     * @param cls
     * @return
     */
    public static <T> List<T> json2List(String json, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        if (gson != null) {
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (final JsonElement elem : array) {
                list.add(gson.fromJson(elem, cls));
            }
        }
        return list;
    }


    /**
     * 将json字符串转换成List
     * 泛型解析
     * @param json  [{},{}]
     * @param cls
     * @return
     */
    public static <T> List<T> jsonType2List(String json, Class<T> cls){
        return gson.fromJson(json, new TypeToken<List<T>>() {}.getType());
    }


    /**
     * 将json字符串转换成 map
     * 泛型解析
     * @param json  {"":"","":{},"":[]}
     * @return
     */
    public static <K,V> HashMap<K,V> jsonType2Map(String json,Class<K> kClass,Class<V> vClass){
        return  gson.fromJson(json,new TypeToken<HashMap<K,V>>(){}.getType());
    }


    /**
     * 将JsonObject对象转换成Map集合
     *
     * @param json
     * @return
     */
    public static HashMap<String, Object> toMap(JsonObject json) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();
        for (Iterator<Map.Entry<String, JsonElement>> iter = entrySet.iterator(); iter
                .hasNext();) {
            Map.Entry<String, JsonElement> entry = iter.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof JsonArray)
                map.put((String) key, toList((JsonArray) value));
            else if (value instanceof JsonObject)
                map.put((String) key, toMap((JsonObject) value));
            else if(value instanceof JsonNull)
                map.put((String) key, null);
            else
//				map.put((String) key, urlDecode(entry.getValue().getAsString()));//value
                map.put((String) key,entry.getValue().getAsString());//value
        }
        return map;
    }

    /**
     * 将JsonArray对象转换成List集合
     *
     * @param json
     * @return
     */
    public static List<Object> toList(JsonArray json) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < json.size(); i++) {
            Object value = json.get(i);
            if (value instanceof JsonArray) {
                list.add(toList((JsonArray) value));
            } else if (value instanceof JsonObject) {
                list.add(toMap((JsonObject) value));
            } else {
                list.add(value);
            }
        }
        return list;
    }


    /**
     * 获取JsonObject
     *
     * @param json
     * @return
     */
    public static JsonObject parseJson(String json) {
        //System.out.println("parseJson-"+json);
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(json).getAsJsonObject();
        return jsonObj;
    }

    /**
     * 获取json数据中的key关键字
     * @param jsonObject
     * @param key
     * @return
     */
    public static String getString(JsonObject jsonObject, String key){
        JsonElement element = jsonObject.get(key);
        String value = null;
        if(element!=null){
            value = element.getAsString();
        }
        return value;
    }
}
