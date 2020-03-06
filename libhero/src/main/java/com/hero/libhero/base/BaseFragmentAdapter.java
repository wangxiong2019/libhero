package com.hero.libhero.base;


import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 */
public abstract class BaseFragmentAdapter<T extends Fragment> extends FragmentPagerAdapter {

    public List<T> mFragmentSet = new ArrayList<>(); // Fragment集合

    private T mCurrentFragment; // 当前显示的Fragment

    /**
     * 在Activity中使用ViewPager适配器
     */
    public BaseFragmentAdapter(FragmentActivity activity) {
        this(activity.getSupportFragmentManager());
    }

    /**
     * 在Fragment中使用ViewPager适配器
     */
    public BaseFragmentAdapter(Fragment fragment) {
        this(fragment.getChildFragmentManager());
    }

    public BaseFragmentAdapter(FragmentManager manager) {
        super(manager);

    }

    //初始化Fragment
    public void addList(List<T> list) {
        mFragmentSet.clear();
        mFragmentSet.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public T getItem(int position) {
        return mFragmentSet.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentSet.size();
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (getCurrentFragment() != object) {
            // 记录当前的Fragment对象
            mCurrentFragment = (T) object;
        }
        super.setPrimaryItem(container, position, object);
    }

    /**
     * 获取Fragment集合
     */
    public List<T> getAllFragment() {
        return mFragmentSet;
    }

    /**
     * 获取当前的Fragment
     */
    public T getCurrentFragment() {
        return mCurrentFragment;
    }
}