package com.hero.launchmode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hero.BaseActivty;
import com.hero.R;
import com.hero.libhero.utils.singleclick.SingleClick;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创建 by hero
 * 时间 2020/5/26
 * 类名
 */
public class LaunchModeAc extends BaseActivty {



    @BindView(R.id.tv_standard)
    TextView tvStandard;
    @BindView(R.id.tv_singleTop)
    TextView tvSingleTop;
    @BindView(R.id.tv_singleTask)
    TextView tvSingleTask;
    @BindView(R.id.tv_singleInstance)
    TextView tvSingleInstance;

    @Override
    public int getLayout() {
        return R.layout.ac_launchmode;
    }

    @Override
    public void initView() {
    }



    @SingleClick
    @OnClick({R.id.tv_standard, R.id.tv_singleTop, R.id.tv_singleTask, R.id.tv_singleInstance})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_standard:
                intent=new Intent(mContext,StandardAc.class);
                intent.putExtra("id","1");
                startActivity(intent);
                break;
            case R.id.tv_singleTop:
                intent=new Intent(mContext,SingleTopAc.class);
                intent.putExtra("id","1");
                startActivity(intent);
                break;
            case R.id.tv_singleTask:
                intent=new Intent(mContext,SingleTaskAc.class);
                intent.putExtra("id","1");
                startActivity(intent);
                break;
            case R.id.tv_singleInstance:
                intent=new Intent(mContext,SingleInstanceAc.class);
                intent.putExtra("id","1");
                startActivity(intent);
                break;
        }
    }
}
