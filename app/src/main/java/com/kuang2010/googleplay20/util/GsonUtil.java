package com.kuang2010.googleplay20.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
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
     * 将 object 转换成 json字符串
     *
     * @param object  bean对象或者Map对象
     * @return
     */
    public static String object2String(Object object) {
        return gson.toJson(object);
    }



    /**
     * 将 json字符串转换成 javabean
     * Bean解析
     * @param jsonString {"":"","":{},"":[]}
     * @param cls 泛型T所对应的字节码对象
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
     * @param cls 泛型T所对应的字节码对象
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
     * @param type new TypeToken<List<T>>() {}.getType()
     * @return
     */
    public static <T> List<T> jsonType2List(String json,Type type){
        return gson.fromJson(json, type);//gson.fromJson(json, new TypeToken<List<T>>() {}.getType());
    }


    /**
     * 将json字符串转换成 map
     * 泛型解析
     * @param json  {"":"","":{},"":[]}
     * @param type new TypeToken<HashMap<K,V>>(){}.getType()
     * @return
     */
    public static <K,V> HashMap<K,V> jsonType2Map(String json,Type type){
        return  gson.fromJson(json, type);//gson.fromJson(json,new TypeToken<HashMap<K,V>>(){}.getType());
    }

    /**
     * 将json字符串转换成任意对象T
     * 通用解析(泛型解析),通过反射拿到泛型的具体类型
     * @param result  json字符串 "{}" "[]"
     * @param o 声明了泛型T的类的 子类对象o。在创建 子类对象o时要传入具体类型 ,
     *          如 Object o = new Object<Bean>(){};内部类也是个子类
     * @return 任一Bean对象
     */
    public static  <T>T json2T(String result,Object o){
        //通用解析：通过反射拿到cls类声明的所有泛型的具体类型
        ParameterizedType genericSuperclass = (ParameterizedType) o.getClass().getGenericSuperclass();// 获取当前new的对象的父类类型
        Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();//所有泛型类型
        Type actualTypeArgument = actualTypeArguments[0];//默认第0个泛型对应的具体类型就是要返回的类型
        return new Gson().fromJson(result,actualTypeArgument);
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
