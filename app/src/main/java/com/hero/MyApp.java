package com.hero;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.hero.libhero.LibHeroInitializer;
import com.hero.libhero.okhttp.OkHttpUtil;

import java.util.HashMap;
import java.util.Map;

public class MyApp extends Application {

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        //360插件化 第三步
//        RePlugin.App.attachBaseContext(this);
//    }

    @Override
    public void onCreate() {
        super.onCreate();

        //RePlugin.App.onCreate();

        LibHeroInitializer.init(this,true);

        initOkHttp();
    }

//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//
//        /* Not need to be called if your application's minSdkVersion > = 14 */
//        RePlugin.App.onLowMemory();
//
//    }
//
//    @Override
//    public void onTrimMemory(int level) {
//        super.onTrimMemory(level);
//
//        /* Not need to be called if your application's minSdkVersion > = 14 */
//        RePlugin.App.onTrimMemory(level);
//
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration config) {
//        super.onConfigurationChanged(config);
//
//        /* Not need to be called if your application's minSdkVersion > = 14 */
//        RePlugin.App.onConfigurationChanged(config);
//    }



    private void initOkHttp() {
        Map<String, String> map_header = new HashMap<>();
        map_header.put("a_token", "1234");
        map_header.put("content-type", "application/x-www-form-urlencoded");
        new OkHttpUtil().setHeaderMap(map_header);
        OkHttpUtil.initOkHttp();
    }
}
