package com.hero.libhero.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.hero.libhero.LibHeroInitializer;
import com.hero.libhero.mydb.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ActivityUtil {



    public static ArrayList<Activity> activityList = new ArrayList<Activity>();

    public static void AddAct(Activity act) {

        if (activityList != null && activityList.size() > 0) {
            if (!activityList.contains(act)) {
                activityList.add(act);
            }
        } else {
            activityList.add(act);
        }


//        //先判断act是否存在  activitys
//        for (int i = 0; i < activitys.size(); i++) {
//
//            int m = act.toString().lastIndexOf(".");
//            String act_name = act.toString().substring((m + 1));
//            String[] act_names = act_name.split("@");
//
//            int n = activitys.get(i).toString().lastIndexOf(".");
//            String act_name2 = activitys.get(i).toString().substring((n + 1));
//            String[] act_names2 = act_name2.split("@");
//
//            Log.e("act_name=", act_name + "//act_name2=" + act_name2);
//
//            if (act_names[0].equals(act_names2[0])) {
//                FinishActivity(activitys.get(i));
//                Log.e("删除了已存在的act=", act.toString());
//            }
//        }
//
//        activitys.add(act);

        LogUtil.e("ActivityUtil=activitys.size()==" + activityList.size() + "//act==" + act);
    }

    public static void FinishAct(Activity act) {

        if (act != null && activityList != null && activityList.size() > 0) {
            activityList.remove(act);
            act.finish();
        }

//        for (int i = 0; i < activitys.size(); i++) {
//
//            int m = act.toString().lastIndexOf(".");
//            String act_name = act.toString().substring((m + 1));
//            String[] act_names = act_name.split("@");
//
//            int n = activitys.get(i).toString().lastIndexOf(".");
//            String act_name2 = activitys.get(i).toString().substring((n + 1));
//            String[] act_names2 = act_name2.split("@");
//
//            Log.e("act_name=", act_name + "//act_name2=" + act_name2);
//
//            if (act_names[0].equals(act_names2[0])) {
//                activitys.remove(activitys.get(i));
//
//                Log.e("删除了act=", act.toString());
//            }
//        }


        LogUtil.e("ActivityUtil=activitys.size()==" + activityList.size() + "//act==" + act);
    }

    public static void ExitApp() {


        for (Activity wActivity : activityList) {
            if (wActivity != null) {
                wActivity.finish();
            }
        }
        activityList.clear();

    }


    public static String GetVersion() {
        String version = "";
        try {
            version = LibHeroInitializer.appContext.getPackageManager().getPackageInfo(LibHeroInitializer.appContext.getPackageName(), 0).versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }
        return version;
    }

    public static int GetVersionCode() {
        int versionCode = 1;
        try {
            versionCode = LibHeroInitializer.appContext.getPackageManager().getPackageInfo(LibHeroInitializer.appContext.getPackageName(), 0).versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    public static String mSavePath = Environment.getExternalStorageDirectory() + "/app_"+getAppLastName();


    public static void IsHasSD() {

        //判断SD卡是否挂载：
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File filepath = new File(mSavePath);
            if (!filepath.exists()) {
                filepath.mkdirs();
            }
        } else {
            Toast.makeText(LibHeroInitializer.appContext, "SD卡异常", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getAppLastName() {
        String packname = LibHeroInitializer.appContext.getPackageName();
        int m = packname.lastIndexOf(".");
        String name = packname.substring(m + 1);
        return name;
    }

    public static String getAppFullName() {
        String packname = LibHeroInitializer.appContext.getPackageName();

        return packname;
    }

    /**
     * 根据包名检测某个APP是否安装
     * <h3>Version</h3> 1.0
     *
     * @param packageName 包名
     * @return true 安装 false 没有安装
     */
    public static boolean isInstallByRead(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}
