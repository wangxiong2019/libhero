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
 * 如果在栈中已经有该Activity的实例，就重用该实例(会调用实例的onNewIntent())。重用时，
 * 会让该实例回到栈顶，因此在它上面的实例将会被移除栈。如果栈中不存在该实例，将会创建新的实例放入栈中。
 *
 * singleTask适合作为程序入口点。例如浏览器的主界面。不管从多少个应用启动浏览器，只会启动主界面一次，
 *      其余情况都会走onNewIntent，并且会清空主界面上面的其他页面。之前打开过的页面，打开之前的页面就ok，不再新建。
 */
public class SingleTaskAc extends BaseActivty {
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
        intent=new Intent(mContext,SingleTaskAc.class);
        intent.putExtra("id","1");
        startActivity(intent);
    }
}
