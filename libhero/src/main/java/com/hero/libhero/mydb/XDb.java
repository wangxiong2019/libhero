package com.hero.libhero.mydb;

import android.app.Application;
import android.content.Context;

import com.hero.libhero.mydb.dbinit.other.DbManager;
import com.hero.libhero.mydb.dbinit.other.DbManagerImpl;

import java.lang.reflect.Method;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;


public class XDb {
    private XDb() {
    }
    public static boolean logShow=false;

    public static boolean isLogShow() {
        return logShow;
    }

    public static void setLogShow(boolean logShow) {
        XDb.logShow = logShow;
    }



    public static Application app() {
        if (Ext.app == null) {
            try {
                // 在IDE进行布局预览时使用
                Class<?> renderActionClass = Class.forName("com.android.layoutlib.bridge.impl.RenderAction");
                Method method = renderActionClass.getDeclaredMethod("getCurrentContext");
                Context context = (Context) method.invoke(null);
                Ext.app = new MockApplication(context);
            } catch (Throwable ignored) {
                throw new RuntimeException("please invoke x.Ext.init(app) on Application#onCreate()"
                        + " and register your Application in manifest.");
            }
        }
        return Ext.app;
    }


    public static DbManager getDb(DbManager.DaoConfig daoConfig) {
        return DbManagerImpl.getInstance(daoConfig);
    }

    public static class Ext {
        private static Application app;


        private Ext() {
        }


        public static void init(Application app,boolean logShow) {
            if (Ext.app == null) {
                Ext.app = app;

                setLogShow(logShow);
            }
        }


    }




    private static class MockApplication extends Application {
        public MockApplication(Context baseContext) {
            this.attachBaseContext(baseContext);
        }
    }
}

