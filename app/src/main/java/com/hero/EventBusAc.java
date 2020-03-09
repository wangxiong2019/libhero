package com.hero;

import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class EventBusAc extends BaseActivty {
    @BindView(R.id.tv_01)
    TextView tv_01;

    @Override
    public int getLayout() {
        return R.layout.fragment_butterknife;
    }

    @Override
    public void initView() {
        tv_01.setText("I am a button ");

        //4.发送事件
        EventBus.getDefault().post(new WxCodeEB("发送事件"));
    }



    @OnClick(R.id.tv_01)
    public void onClick() {
    }
}
