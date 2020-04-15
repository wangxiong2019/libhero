package com.hero;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hero.adapter.ImgAdapter_RV;
import com.hero.libhero.base.GridSpaceItemDecoration;
import com.hero.libhero.photopicker.PhotoPicker;
import com.hero.libhero.utils.PhotoUtil;
import com.hero.libhero.utils.SharedUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创建 by hero
 * 时间 2020/4/15
 * 类名 图片选择 以及放大查看
 */
public class SelectImgAc extends BaseActivty {
    @BindView(R.id.gv_menu)
    RecyclerView gv_menu;
    @BindView(R.id.tv_upload)
    TextView tvUpload;

    @Override
    public int getLayout() {
        return R.layout.ac_selectimg;
    }

    List<String> imgList = new ArrayList<>();
    ImgAdapter_RV adapter_rv;

    @Override
    public void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 3);
        gv_menu.setLayoutManager(gridLayoutManager);
        gv_menu.addItemDecoration(new GridSpaceItemDecoration(25));

        adapter_rv = new ImgAdapter_RV(mActivity);
        gv_menu.setAdapter(adapter_rv);

    }


    @OnClick(R.id.tv_upload)
    public void onClick() {

        PhotoUtil.SelectCamera(mActivity, 9 - imgList.size(), 1, 2);

    }

    String imgpath1 = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == RESULT_OK) {
            switch (requestCode) {


                case 1:
                    adapter_rv.clear();
                    imgpath1 = SharedUtil.getString(PhotoUtil.Img_Key);

                    imgList.add(imgpath1);
                    adapter_rv.addData(imgList);
                    break;
                case 2:
                    if (data != null) {
                        adapter_rv.clear();
                        List<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                        imgList.addAll(photos);
                        adapter_rv.addData(imgList);
                    }
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
