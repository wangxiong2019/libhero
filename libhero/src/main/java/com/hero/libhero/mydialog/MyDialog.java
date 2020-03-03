package com.hero.libhero.mydialog;

import android.content.Context;
import android.util.Log;

/**
 * Created by Administrator on 2017/8/4.
 */
public  class MyDialog extends BaseMyDialog {
    String TAG = "MyDialog";

    public MyDialog(Context context) {
        super(context);
        init();
    }

    @Override
    public void init() {
        Log.e(TAG, "windowStyle");
        windowStyle();
    }
}
