package com.hero.libhero.mydialog;

import android.content.Context;
import android.util.Log;

/**
 * Created by Administrator on 2017/8/4.
 */
public class MyDialogWithType extends BaseMyDialog {
    String TAG = "MyDialogWithType";

    public MyDialogWithType(Context context) {
        super(context);
        init();
    }

    @Override
    public void init() {
        Log.e(TAG, "windowStyleWithType");
        windowStyleWithType();
    }
}
