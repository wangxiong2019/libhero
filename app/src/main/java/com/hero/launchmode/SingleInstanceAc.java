package com.hero.launchmode;

import android.content.Intent;
import android.widget.TextView;

import com.hero.BaseActivty;
import com.hero.R;
import com.hero.libhero.mydb.LogUtil;
import com.hero.libhero.view.XToast;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建 by hero
 * 时间 2020/5/26
 * 类名
 *
 * 在一个新栈中创建该Activity实例，并让多个应用共享改栈中的该Activity实例。
 * 一旦改模式的Activity的实例存在于某个栈中，任何应用再激活改Activity时都会重用该栈中的实例，
 * 其效果相当于多个应用程序共享一个应用，不管谁激活该Activity都会进入同一个应用中。
 *
 *
 * singleInstance适合需要与程序分离开的页面。例如闹铃提醒，将闹铃提醒与闹铃设置分离。singleInstance不要用于中间页面，
 *       如果用于中间页面，跳转会有问题，比如：A -> B (singleInstance) -> C，完全退出后，在此启动，首先打开的是B。
 */
public class SingleInstanceAc extends BaseActivty {
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
        LogUtil.e("每次都执行onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e("onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.e("退到后台 重新进入页面执行onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.e("第一次进入这个页面执行onStart  以后都不执行");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("页面销毁onDestroy");
    }

    @OnClick(R.id.tv_name)
    public void onClick() {
        intent=new Intent(mContext,SingleInstanceAc.class);
        intent.putExtra("id","1");
        startActivity(intent);
    }
}
