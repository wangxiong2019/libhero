package com.hero.libhero.base;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class BaseSimpleAdapter<T> extends BaseAdapter {
    public Activity mActivity;
    public List<T> list = new ArrayList<>();
    public String TAG = getClass().getSimpleName();

    public BaseSimpleAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }


    public void addData(List<T> list) {
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }


    /**
     * @param list    新数据的集合
     * @param isFirst 是否是第一页的数据
     */
    public void addData(List<T> list, boolean isFirst) {
        if (isFirst) {
            this.list.clear();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    public void clear() {
        for (int i = 0; i < list.size(); i++) {
            list.remove(i);
        }
        list = new ArrayList<>();
        this.notifyDataSetChanged();
    }

    public void remove(int pos) {
        list.remove(pos);

        this.notifyDataSetChanged();
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return simpleGetView(position, convertView, parent);
    }

    public abstract View simpleGetView(int position, View convertView, ViewGroup parent);
}
