package com.hero.libhero.base;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 *
 */
public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter<BaseRecyclerHolder> {
    public Activity mActivity;
    public Intent intent;
    public List<T> list = new ArrayList<>();
    public String TAG = getClass().getSimpleName();

    public static final int TYPE_BANNER_HEAD = 0;
    public static final int TYPE_COMMON = 1;
    public static final int TYPE_BANNER_FOOT = 2;


    private int layoutId;

    public BaseRVAdapter(Activity mActivity, int layoutId) {
        this.mActivity = mActivity;
        this.layoutId = layoutId;
    }

    //此函数用来创建每一个item，最后返回的不是view，而是返回的一个ViewHolder。
    @Override
    public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = generateItemView(parent, viewType);// LayoutInflater.from(mActivity).inflate(layoutId, parent, false);
        return new BaseRecyclerHolder(itemView);
    }

    //此函数中一般用来将数据绑定到item中的控件中。
    @Override
    public void onBindViewHolder(final BaseRecyclerHolder holder, int position) {
        bindViewHolder(holder, list.get(position), position);
    }

    protected View generateItemView(ViewGroup parent, int viewType) {
        return getInflate(parent, layoutId);
    }

    /**
     * 加载布局获取控件
     */
    protected View getInflate(ViewGroup parent, @LayoutRes int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public abstract void bindViewHolder(BaseRecyclerHolder mHolder, T bean, int position);


    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
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

    public interface ItemListener {

        /**
         * 单击图片事件
         */
        void onItemClick(int position);

    }
}
