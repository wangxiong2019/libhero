package com.hero.libhero.base;


import android.view.View;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 *
 */
public class ViewPagerNoTabAdapter extends PagerAdapter {
    private List<View> views;

    public ViewPagerNoTabAdapter(List<View> views) {
        this.views = views;
    }

    /**
     *
     */
    @Override
    public int getCount() {
        return views.size();

    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }


    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(views.get(position));
    }


    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(views.get(position), 0);
        return views.get(position);
    }

}
