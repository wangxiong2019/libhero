package com.hero.libhero;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class LibHeroInitializer {

    /**
     * 1.
     * allprojects {
     *     repositories {
     *         maven {
     *             url 'https://jitpack.io'
     *         }
     *     }
     * }
     *  2.
     *  implementation 'com.github.wangxiong2019:libhero:v1.0.1'
     *
     *  3. Application
     *  LibHeroInitializer.init(this)
     */




    public static Context appContext;// application context
    public static Handler mainHandler = new Handler(Looper.getMainLooper());// 主线程的Handler

    /**
     * @Description 使用依赖库时必须在Application的onCreate方法中显式调用ToastInitializer.initializer(this);
     */
    public static void init(Context context) {
        if (null != context) {
            appContext = context;
        }
    }

    public static void runOnUiThread(Runnable task) {
        mainHandler.post(task);
    }

    public static void runOnUiThreadDelayed(Runnable task, long miliis) {
        mainHandler.postDelayed(task, miliis);
    }
}
