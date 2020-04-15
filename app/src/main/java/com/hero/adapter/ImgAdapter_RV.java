package com.hero.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.hero.R;
import com.hero.libhero.base.BaseRVAdapter;
import com.hero.libhero.base.BaseRecyclerHolder;
import com.hero.libhero.imageview.preview.PreviewBuilder;
import com.hero.libhero.imageview.preview.enitity.ImageViewInfo;
import com.hero.libhero.utils.GlideUtil;
import com.hero.libhero.utils.GlobalUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 创建 by hero
 * 时间 2020/4/3
 * 类名
 */
public class ImgAdapter_RV extends BaseRVAdapter<String> {

    public ImgAdapter_RV(Activity mActivity) {
        super(mActivity, R.layout.gv_img_item);
    }


    @Override
    public void bindViewHolder(BaseRecyclerHolder mHolder, String bean, int position) {
        ImageView iv_pic = mHolder.findViewById(R.id.iv_pic);

        String img = bean;
        GlideUtil.loadGifOrImg(mActivity, img, iv_pic);


        iv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalUtil.isFastDoubleClick() == false) {
                    Log.e(TAG, "禁止双击");

                    List<ImageViewInfo> mUrlList = new ArrayList<>();
                    for (String imgurl : getList()) {
                        mUrlList.add(new ImageViewInfo(imgurl));
                    }
                    PreviewBuilder.from(mActivity)
                            .setImgs(mUrlList)
                            .setCurrentIndex(position)
                            .setSingleFling(true)
                            .setType(PreviewBuilder.IndicatorType.Number)
                            .start();

                    return;
                }
            }
        });

    }


}
