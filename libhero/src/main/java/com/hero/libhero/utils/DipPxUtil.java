package com.hero.libhero.utils;

import android.content.Context;
import android.view.WindowManager;

import com.hero.libhero.LibHeroInitializer;


public class DipPxUtil {

    public static final int NORMAL = 1;

    public static int dp2px(float dpValue) {
        final float scale = LibHeroInitializer.appContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static int px2dp(float pxValue) {
        final float scale = LibHeroInitializer.appContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) LibHeroInitializer.appContext
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }


    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) LibHeroInitializer.appContext
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }






}
