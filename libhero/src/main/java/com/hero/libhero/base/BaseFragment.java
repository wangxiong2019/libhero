package com.hero.libhero.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    public String TAG = "";
    public Activity mActivity;
    public Context mContext;
    public Intent intent;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mContext = getContext();
        TAG = mActivity.getLocalClassName();
    }
}
