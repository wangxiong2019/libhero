package com.hero.libhero.base;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

/**
 *
 */
public class ViewPagerFragNoTabAdapter extends FragmentPagerAdapter {
    List<Fragment> mFragments = new ArrayList<>();
    FragmentManager fm;

    public ViewPagerFragNoTabAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;

    }

    public void addData(List<Fragment> mFragments) {

        this.mFragments = mFragments;
        notifyDataSetChanged();
    }

    public void clear() {
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
    public Fragment getItem(int position) {
        return mFragments == null ? null : mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }


    @Override
    public long getItemId(int position) {
        return mFragments.get(position).hashCode();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (mFragments.contains(object)) {
            // 如果当前 item 未被 remove，则返回 item 的真实 position
            return mFragments.indexOf(object);
        } else {
            // 否则返回状态值 POSITION_NONE
            return POSITION_NONE;
        }
    }

}
