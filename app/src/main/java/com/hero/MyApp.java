package com.hero;

import android.app.Application;

import com.hero.libhero.LibHeroInitializer;
import com.hero.libhero.okhttp.OkHttpUtil;

import java.util.HashMap;
import java.util.Map;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LibHeroInitializer.init(this);

        initOkHttp();
    }

    private void initOkHttp() {
        Map<String, String> map_header = new HashMap<>();
        map_header.put("a_token", "1234");
        map_header.put("content-type", "application/x-www-form-urlencoded");
        new OkHttpUtil().setHeaderMap(map_header);
        OkHttpUtil.initOkHttp();
    }
}
