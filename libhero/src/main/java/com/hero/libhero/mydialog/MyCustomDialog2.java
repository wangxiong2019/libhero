package com.hero.libhero.mydialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.hero.libhero.R;


public class MyCustomDialog2 extends Dialog {
    //    style引用style样式
    public MyCustomDialog2(Context context, View layout, float mWidthScale1) {

        super(context, R.style.MyDialogTheme);

        ViewGroup.LayoutParams layoutParams;
        DisplayMetrics mDisplayMetrics;// (DisplayMetrics)设备密度
        float mWidthScale = 0.95f;
        if(mWidthScale1!=0){
            mWidthScale =mWidthScale1;
        }

        mDisplayMetrics = context.getResources().getDisplayMetrics();
        int width = (int) (mDisplayMetrics.widthPixels * mWidthScale);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams = new ViewGroup.LayoutParams(width, height);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);

        window.setContentView(layout,layoutParams);

    }
}
