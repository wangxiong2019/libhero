package com.hero;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.hero.libhero.utils.ActivityUtil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;

public abstract class BaseActivty extends AppCompatActivity {
    public String TAG;
    public Activity mActivity;
    public Context mContext;
    public Intent intent;

    public  String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mActivity = this;
        mContext = this;

        ActivityUtil.AddAct(this);
        TAG = getLocalClassName();

        ButterKnife.bind(this);

        initView();


    }

    public abstract int getLayout();
    public abstract void initView();

    //按返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            ActivityUtil.FinishAct(mActivity);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
