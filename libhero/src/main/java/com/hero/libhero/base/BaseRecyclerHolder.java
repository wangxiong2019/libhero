package com.hero.libhero.base;


import android.util.SparseArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class BaseRecyclerHolder extends RecyclerView.ViewHolder {
    private View itemView;
    private SparseArray<View> viewSparseArray;

    public BaseRecyclerHolder(View itemView) {//Context context,
        super(itemView);
        this.itemView = itemView;
        this.viewSparseArray = new SparseArray<View>();
    }

    public View getItemView() {
        return itemView;
    }

    public <T extends View> T findViewById(int viewId) {
        View view = viewSparseArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            viewSparseArray.put(viewId, view);
        }
        return (T) view;
    }
}
