package com.hero.libhero.base;

import android.graphics.Rect;
import android.view.View;

import com.hero.libhero.mydb.LogUtil;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 创建 by hero
 * 时间 2020/4/3
 * 类名 水平垂直间距
 */
public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int v_space;
    private int h_space;

    //水平垂直间距一样
    public GridSpaceItemDecoration(int v_space) {
        this.v_space = v_space;
        this.h_space = v_space;
    }

    //水平垂直间距不一样
    public GridSpaceItemDecoration(int v_space, int h_space) {
        this.v_space = v_space;
        this.h_space = h_space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = v_space;
        outRect.bottom = h_space;
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int num = layoutManager.getSpanCount();
        LogUtil.e("layoutManager--num=" + num);
        if (parent.getChildLayoutPosition(view) % num == 0) {
            outRect.left = 0;
        }
    }
}
