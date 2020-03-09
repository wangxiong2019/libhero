package com.hero;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ButterknifeFragment extends Fragment {


    @BindView(R.id.tv_01)
    TextView tv_01;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_butterknife, container, false);

        //绑定fragment
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.tv_01)
    public void onClick() {
    }
}
