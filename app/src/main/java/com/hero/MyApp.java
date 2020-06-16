package com.hero;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.hero.libhero.LibHeroInitializer;
import com.hero.libhero.keepalive.AliveJobService;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MyApp extends Application {
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        boolean isDebug = isApkInDebug(this);

        LibHeroInitializer.init(this, isDebug);

        initCrash();

        //保活服务
        //pullAliveService();

        initARouter(isDebug);
    }
    private void initARouter(boolean isDebug){
        Log.e("BaseApplication", "isDebug=" + isDebug);
        if (isDebug) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }

    /**
     * 判断当前应用是否是debug状态
     */

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }


    private void initCrash() {
// 获取当前包名
        String packageName = context.getPackageName();
// 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
// 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
// 初始化Bugly
        CrashReport.initCrashReport(context, "a93d992f1e", true, strategy);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    private void pullAliveService() {
        AliveJobService.start(context);
    }


}
