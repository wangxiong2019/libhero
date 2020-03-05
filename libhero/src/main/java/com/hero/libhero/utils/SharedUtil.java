package com.hero.libhero.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hero.libhero.LibHeroInitializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * 封装SharedPreference的工具类。
 */
public class SharedUtil {

    public static String AppName = ActivityUtil.getAppLastName();


    public static void putString(String key, String value) {
        //声明并获取一个SharedPreferences对象，模式为私有模式

        Context context = LibHeroInitializer.appContext;
        SharedPreferences sp = context.getSharedPreferences(AppName, Context.MODE_PRIVATE);

        if (TextUtils.isEmpty(value)) {
            sp.edit().putString(key, "").commit();
        } else {
            sp.edit().putString(key, value).commit();
        }

    }

    public static String getString(String key) {
        Context context = LibHeroInitializer.appContext;
        SharedPreferences sp = context.getSharedPreferences(AppName, Context.MODE_PRIVATE);
        //defValue 如果没有得到值就返回defValue，这是没有值时防止返回null。
        String value = sp.getString(key, "");

        return value;
    }

    public static void putInt(String key, int value) {
        //声明并获取一个SharedPreferences对象，模式为私有模式
        Context context = LibHeroInitializer.appContext;
        SharedPreferences sp = context.getSharedPreferences(AppName, Context.MODE_PRIVATE);

        sp.edit().putInt(key, value).commit();

    }
    public static void putBoolean(String key, boolean value) {
        //声明并获取一个SharedPreferences对象，模式为私有模式
        Context context = LibHeroInitializer.appContext;
        SharedPreferences sp = context.getSharedPreferences(AppName, Context.MODE_PRIVATE);

        sp.edit().putBoolean(key, value).commit();

    }
    public static Boolean getBoolean(String key) {
        Context context = LibHeroInitializer.appContext;
        SharedPreferences sp = context.getSharedPreferences(AppName, Context.MODE_PRIVATE);
        //defValue 如果没有得到值就返回defValue，这是没有值时防止返回null。
        boolean value = sp.getBoolean(key, false);

        return value;
    }

    public static int getInt(String key) {
        Context context = LibHeroInitializer.appContext;
        SharedPreferences sp = context.getSharedPreferences(AppName, Context.MODE_PRIVATE);
        //defValue 如果没有得到值就返回defValue，这是没有值时防止返回null。
        int value = sp.getInt(key, 0);

        return value;
    }

    public static void putFloat(String key, float value) {
        //声明并获取一个SharedPreferences对象，模式为私有模式
        Context context = LibHeroInitializer.appContext;
        SharedPreferences sp = context.getSharedPreferences(AppName, Context.MODE_PRIVATE);

        sp.edit().putFloat(key, value).commit();

    }

    public static float getFloat(String key) {
        Context context = LibHeroInitializer.appContext;
        SharedPreferences sp = context.getSharedPreferences(AppName, Context.MODE_PRIVATE);
        //defValue 如果没有得到值就返回defValue，这是没有值时防止返回null。
        float value = sp.getFloat(key, 0);

        return value;
    }

    public static void clearData(String str) {
        Context context = LibHeroInitializer.appContext;
        SharedPreferences sp = context.getSharedPreferences(AppName, Context.MODE_PRIVATE);
        sp.edit().remove(str).commit();
    }


    /**
     * 保存对象
     *
     * @param key
     * @param object
     * @return
     */
    public static boolean putObjectData(String key, Object object) {
        boolean result;
        try {


            String json = JSON.toJSONString(object);
            putString(key, json);
            result = true;
        } catch (Exception e) {
            result = false;
        }

        return result;
    }

    /**
     * 获取对象
     *
     * @param key
     * @param clsV
     * @param <T>
     * @return
     */
    public static <T> T getObjectData(String key, Class<T> clsV) {
        try {
            String json = getString(key);
            if (!json.equals("") && json.length() > 0) {

                T t = JSON.parseObject(json, clsV);
                return t;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }


    }



    /**
     * 用于保存集合
     *
     * @param key key
     * @param map map数据
     * @return 保存结果
     */
    public  <K, V> boolean putHashMapData(String key, Map<K, V> map) {
        boolean result;

        try {

            String json = JSON.toJSONString(map);
            putString(key, json);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 用于保存集合
     *
     * @param key key
     * @return HashMap
     */
    public  <V> HashMap<String, V> getHashMapData(String key, Class<V> clsV) {
        String json = getString(key);
        HashMap<String, V> map = new HashMap<>();
        JSONObject obj = JSONObject.parseObject(json);


        Set<Map.Entry<String, Object>> entrySet = obj.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            String entryKey = entry.getKey();
            Object value = (Object) entry.getValue();
            map.put(entryKey, (V)value);
        }
        //Log.e("SharedPreferencesUtil", obj.toString());
        return map;
    }
}
