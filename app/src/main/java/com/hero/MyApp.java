package com.hero;

import android.app.Application;

import com.hero.libhero.LibHeroInitializer;
import com.hero.libhero.keepalive.AliveJobService;

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

        LibHeroInitializer.init(this, true);

        //保活服务
        pullAliveService();
    }

    private void pullAliveService() {
        AliveJobService.start(this);
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


}
