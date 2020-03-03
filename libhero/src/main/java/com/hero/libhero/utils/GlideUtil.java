package com.hero.libhero.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;


/**
 * 图片架子工具类
 */
public class GlideUtil {

    public static void loadImage(Context context, String url, int errorId, int placeholderId, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(placeholderId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)


                .error(errorId);

        Glide.with(context).load(url).apply(requestOptions).into(imageView);

    }

    public static void loadImage(Context context, File url, int errorId, int placeholderId, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(placeholderId)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .error(errorId);

        Glide.with(context).load(url).apply(requestOptions).into(imageView);

    }

//    //type 1 圆 2 方 3 椭圆 4 长
//
//    public static void loadGifImg(Context context, String url, ImageView imageView, int type) {
//
//        Log.e("loadGifImg", url+"");
//
//        if (url.contains(".gif")) {
//            Glide.with(context).asGif().load(url).apply(getRequestOptions(type)).into(imageView);
//        } else {
//            Glide.with(context).load(url).apply(getRequestOptions(type)).into(imageView);
//        }
//
//    }
//
//    static RequestOptions requestOptionsFang = null;
//    static RequestOptions requestOptionsYuan = null;
//    static RequestOptions requestOptionsChang = null;
//    static RequestOptions requestOptionsTuoYuan = null;
//
//    public static void loadFangImage(Context context, String url, ImageView imageView) {
//
//
//        url = url + "_310x310.jpg";
//
//        loadNormalFangImage(context, url, imageView);
//
//    }
//
//    public static RequestOptions getRequestOptions(int type) {
//
//        if (type == 1) {
//            if (requestOptionsYuan == null) {
//                requestOptionsYuan = new RequestOptions()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .placeholder(R.mipmap.img_default_yuan)//图片加载出来前，显示的图片
//                        .fallback(R.mipmap.img_default_yuan) //url为空的时候,显示的图片
//                        .error(R.mipmap.img_default_yuan);//图片加载失败后，显示的图片
//            }
//
//            return requestOptionsYuan;
//        } else if (type == 2) {
//            if (requestOptionsFang == null) {
//                requestOptionsFang = new RequestOptions()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .placeholder(R.mipmap.img_default_fang)//图片加载出来前，显示的图片
//                        .fallback(R.mipmap.img_default_fang) //url为空的时候,显示的图片
//                        .error(R.mipmap.img_default_fang);//图片加载失败后，显示的图片
//            }
//
//            return requestOptionsFang;
//        } else if (type == 3) {
//            if (requestOptionsTuoYuan == null) {
//                requestOptionsTuoYuan = new RequestOptions()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .placeholder(R.mipmap.img_default_tuoyuan)//图片加载出来前，显示的图片
//                        .fallback(R.mipmap.img_default_tuoyuan) //url为空的时候,显示的图片
//                        .error(R.mipmap.img_default_tuoyuan);//图片加载失败后，显示的图片
//            }
//
//            return requestOptionsTuoYuan;
//        } else if (type == 4) {
//
//
//            if (requestOptionsChang == null) {
//                requestOptionsChang = new RequestOptions()
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .placeholder(R.mipmap.img_default_chang)//图片加载出来前，显示的图片
//                        .fallback(R.mipmap.img_default_chang) //url为空的时候,显示的图片
//                        .error(R.mipmap.img_default_chang);//图片加载失败后，显示的图片
//            }
//
//            return requestOptionsChang;
//        }
//        return null;
//    }
//
//    public static void loadNormalYuanImage(Context context, String url, ImageView imageView) {
//
//
//        Glide.with(context).load(url).apply(getRequestOptions(1)).into(imageView);
//
//    }
//    public static void loadNormalFangImage(Context context, String url, ImageView imageView) {
//
//
//        Glide.with(context).load(url).apply(getRequestOptions(2)).into(imageView);
//
//    }
//
//
//    public static void loadYuanImage(Context context, String url, ImageView imageView) {
//
//        url = url + "_310x310.jpg";
//
//        loadNormalYuanImage(context, url, imageView);
//
//    }
//





}
