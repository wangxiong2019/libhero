package com.hero.libhero.base;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 创建 by hero
 * 时间 2020/3/12
 * 类名 分割线
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int firstTop;
    private int lastBottom;
    private int middle;

    public SpaceItemDecoration( int middle) {
        this.firstTop = 0;
        this.lastBottom = 0;
        this.middle = middle;
    }
    public SpaceItemDecoration(int firstTop, int middle, int lastBottom) {
        this.firstTop = firstTop;
        this.lastBottom = lastBottom;
        this.middle = middle;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        if (layoutManager.getOrientation()==LinearLayoutManager.VERTICAL){
            //最后一项
            if (parent.getChildAdapterPosition(view)==layoutManager.getItemCount()-1&& layoutManager.getItemCount()!=1){
                outRect.bottom = lastBottom;
                outRect.top = middle;
            }else if (parent.getChildAdapterPosition(view)==0){
                outRect.top = firstTop;
            }else {
                outRect.top = middle;
            }
        }
    }


}

