package com.hero.libhero.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;


import com.hero.libhero.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/9.
 */
public class FlipTextView extends TextSwitcher implements TextSwitcher.ViewFactory {


    private List<String> demoBeans = new ArrayList<>();
    private int mIndex;

    private static final int AUTO_RUN_FLIP_TEXT = 11;
    private static final int WAIT_TIME = 3500;

    public FlipTextView(Context context) {
        super(context);
        init();
    }

    public FlipTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setFactory(this);
        setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.trans_bottom_to_top_in_fast));
        setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.trans_bottom_to_top_out_fast));
    }


    @Override
    public View makeView() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_index_search_item, null);
        TextView tv_flip = (TextView) view.findViewById(R.id.tv_item_title);

        return view;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUTO_RUN_FLIP_TEXT:

                    if (demoBeans.size() > 1) {
                        mHandler.sendEmptyMessageDelayed(AUTO_RUN_FLIP_TEXT, WAIT_TIME);
                    }
                    setText(demoBeans.get(mIndex));
                    mIndex++;
                    if (mIndex > demoBeans.size() - 1) {
                        mIndex = 0;
                    }

                    break;
            }
        }
    };

    public void setData(List<String> datas) {

        if (demoBeans.size() > 0) {
            demoBeans.clear();
        }
        demoBeans.addAll(datas);
        mIndex = 0;

        mHandler.removeMessages(AUTO_RUN_FLIP_TEXT);
        mHandler.sendEmptyMessage(AUTO_RUN_FLIP_TEXT);

    }

}
