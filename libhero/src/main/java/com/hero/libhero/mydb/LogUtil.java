package com.hero.libhero.mydb;

import android.text.TextUtils;
import android.util.Log;

import static com.hero.libhero.mydb.XDb.logShow;


public class LogUtil {
    public static String customTagPrefix = "libhero_log";


    private LogUtil() {
    }

    private static String generateTag() {
        StackTraceElement caller = new Throwable().getStackTrace()[2];
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public static void d(String content) {
        if (!logShow) return;
        if (TextUtils.isEmpty(content)) return;
        String tag = generateTag();

        Log.d(tag, content);
    }

    public static void d(String content, Throwable tr) {
        if (!logShow) return;
        if (TextUtils.isEmpty(content)) return;
        String tag = generateTag();

        Log.d(tag, content, tr);
    }

    public static void e(String content) {
        if (!logShow) return;
        if (TextUtils.isEmpty(content)) return;
        String tag = generateTag();

        Log.e(tag, content);
    }

    public static void e(String content, Throwable tr) {
        if (!logShow) return;
        String tag = generateTag();

        Log.e(tag, content, tr);
    }

    public static void i(String content) {
        if (!logShow) return;
        if (TextUtils.isEmpty(content)) return;
        String tag = generateTag();

        Log.i(tag, content);
    }

    public static void i(String content, Throwable tr) {
        if (!logShow) return;
        if (TextUtils.isEmpty(content)) return;
        String tag = generateTag();

        Log.i(tag, content, tr);
    }

    public static void v(String content) {
        if (!logShow) return;
        if (TextUtils.isEmpty(content)) return;
        String tag = generateTag();

        Log.v(tag, content);
    }

    public static void v(String content, Throwable tr) {
        if (!logShow) return;
        if (TextUtils.isEmpty(content)) return;
        String tag = generateTag();

        Log.v(tag, content, tr);
    }

    public static void w(String content) {
        if (!logShow) return;
        if (TextUtils.isEmpty(content)) return;
        String tag = generateTag();

        Log.w(tag, content);
    }

    public static void w(String content, Throwable tr) {
        if (!logShow) return;
        if (TextUtils.isEmpty(content)) return;
        String tag = generateTag();

        Log.w(tag, content, tr);
    }

    public static void w(Throwable tr) {
        if (!logShow) return;

        String tag = generateTag();

        Log.w(tag, tr);
    }


    public static void wtf(String content) {
        if (!logShow) return;
        if (TextUtils.isEmpty(content)) return;
        String tag = generateTag();

        Log.wtf(tag, content);
    }

    public static void wtf(String content, Throwable tr) {
        if (!logShow) return;
        if (TextUtils.isEmpty(content)) return;
        String tag = generateTag();

        Log.wtf(tag, content, tr);
    }

    public static void wtf(Throwable tr) {
        if (!logShow) return;
        String tag = generateTag();

        Log.wtf(tag, tr);
    }
}
