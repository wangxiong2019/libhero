package com.hero.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hero.R;
import com.hero.libhero.base.BaseSimpleAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 创建 by hero
 * 时间 2020/4/3
 * 类名
 */
public class StrAdapter_GV extends BaseSimpleAdapter<String> {

    public StrAdapter_GV(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View simpleGetView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.gv_str_item, null);
            mHolder = new ViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        mHolder.tv_name.setText(list.get(position) + "");

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tv_name;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
