package com.hero.libhero.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class JsonUtil {
    public static JSONObject dataToObject(String data) {
        return JSONObject.parseObject(data);
    }

    public static <T> T dataToClass(String data, Class<T> clazz) {
        if (TextUtils.isEmpty(data)) {
            return null;
        } else {
            T t = JSON.parseObject(data, clazz);
            return t;
        }
    }

    public static <T> List<T> dataToList(String data, Class<T> clazz) {

        List<T> list = JSON.parseArray(data, clazz);
        return list;

    }

    public static String objToString(Object object) {
        return JSON.toJSONString(object);
    }
}
