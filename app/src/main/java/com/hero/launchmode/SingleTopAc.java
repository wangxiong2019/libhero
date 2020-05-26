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
 * 如果在任务的栈顶正好存在该Activity的实例， 就重用该实例，
 * 否者就会创建新的实例并放入栈顶(即使栈中已经存在该Activity实例，只要不在栈顶，都会创建实例)。
 *
 * singleTop适合接收通知启动的内容显示页面。例如，某个新闻客户端的新闻内容页面，
 *      如果收到10个新闻推送，每次都打开一个新闻内容页面是很烦人的。
 */
public class SingleTopAc extends BaseActivty {
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
        LogUtil.e("第一次以及onRestart后进入这个页面执行onStart  以后都不执行");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("页面销毁onDestroy");
    }






    @OnClick(R.id.tv_name)
    public void onClick() {
        intent=new Intent(mContext,SingleTopAc.class);
        intent.putExtra("id","1");
        startActivity(intent);
    }
}
