package com.hero.libhero.base;


import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Administrator on 2017/7/22.
 */
public class ViewPagerWithTabAdapter extends FragmentPagerAdapter {

    List<Fragment> mFragments = new ArrayList<>();
    List<String> mTitles=new ArrayList<>();
    FragmentManager fm;

    public ViewPagerWithTabAdapter(FragmentManager fm) {
        super(fm);
        this.fm=fm;

    }

    public void addData(List<Fragment> mFragments, List<String> mTitles) {

        this.mFragments=mFragments;
        this.mTitles=mTitles;
        notifyDataSetChanged();
    }

    public void clear() {
        mTitles.clear();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (null != fragmentTransaction) {
            List<Fragment> fragments = fm.getFragments();
            if (!fragments.isEmpty()) {
                for (int mm = 0; mm < fragments.size(); mm++) {
                    if (null != fragments.get(mm)) {
                        fragmentTransaction.remove(fragments.get(mm)).commitNowAllowingStateLoss();
                    }
                }
            }
        }
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}