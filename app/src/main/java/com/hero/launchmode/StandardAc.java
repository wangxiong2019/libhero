package com.hero.launchmode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hero.BaseActivty;
import com.hero.R;
import com.hero.libhero.mydb.LogUtil;
import com.hero.libhero.view.XToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创建 by hero
 * 时间 2020/5/26
 * 类名    Activty默认的启动模式 不管有没有已存在的实例，都生成新的实例
 */
public class StandardAc extends BaseActivty {
    @BindView(R.id.tv_name)
    TextView tv_name;

    @Override
    public int getLayout() {
        return R.layout.ac_mode;
    }

    @Override
    public void initView() {
        tv_name.setText(this.toString() + "");

        //第一次进入这个页面
        String id = getIntent().getStringExtra("id");
        XToast.success(mContext, "id=" + id).show();
    }
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //第二次及以后进入这个页面
        String id = getIntent().getStringExtra("id");
        LogUtil.e("onNewIntent--->id="+id);
        XToast.warning(mContext, "id=" + id).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.e("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e("onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.e("onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e("onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("onDestroy");
    }

    @OnClick(R.id.tv_name)
    public void onClick() {
        intent=new Intent(mContext,StandardAc.class);
        intent.putExtra("id","1");
        startActivity(intent);
    }
}
