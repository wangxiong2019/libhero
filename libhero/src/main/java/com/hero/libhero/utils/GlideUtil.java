package com.hero.libhero.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hero.libhero.mydb.LogUtil;


/**
 * 图片架子工具类
 */
public class GlideUtil {

    public static void loadImage(Context context, String url, int placeholder,
                                 int fallback, int error, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .placeholder(placeholder)
                .fallback(fallback)
                .error(error);

        Glide.with(context).load(url).apply(requestOptions).into(imageView);

    }

    public static void loadGifOrImg(Context context, String url, ImageView imageView) {

        loadGifOrImgWithRO(context,url,imageView,null);

    }
    public static void loadGifOrImgWithRO(Context context, String url, ImageView imageView, RequestOptions requestOptions) {

        LogUtil.e("loadGifImg=" + url);

        if (url.contains(".gif")) {

            if (requestOptions == null) {
                Glide.with(context).asGif().load(url).into(imageView);
            } else {
                Glide.with(context).asGif().load(url).apply(requestOptions).into(imageView);
            }
        } else {
            if (requestOptions == null) {
                Glide.with(context).load(url).into(imageView);
            } else {
                Glide.with(context).load(url).apply(requestOptions).into(imageView);

            }
        }

    }

    static RequestOptions requestOptions = null;
    private static int placeholder;
    private static int fallback;
    private static int error;

    public static int getPlaceholder() {
        return placeholder;
    }

    public static void setPlaceholder(int placeholder) {
        GlideUtil.placeholder = placeholder;
    }

    public static int getFallback() {
        return fallback;
    }

    public static void setFallback(int fallback) {
        GlideUtil.fallback = fallback;
    }

    public static int getError() {
        return error;
    }

    public static void setError(int error) {
        GlideUtil.error = error;
    }

    public static RequestOptions getRequestOptions() {

        if (getError() == 0) {
            setError(android.R.mipmap.sym_def_app_icon);
        }
        if (getFallback() == 0) {
            setFallback(android.R.mipmap.sym_def_app_icon);
        }
        if (getPlaceholder() == 0) {
            setPlaceholder(android.R.mipmap.sym_def_app_icon);
        }

        if (requestOptions == null) {
            requestOptions = new RequestOptions()
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(getPlaceholder())//图片加载出来前，显示的图片 R.mipmap.img_default_yuan
                    .fallback(getFallback()) //url为空的时候,显示的图片
                    .error(getError());//图片加载失败后，显示的图片 .centerCrop();


        }

        return requestOptions;

    }


    public static RequestOptions getResOptions(int placeholder,int fallback,int error) {

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(placeholder)//图片加载出来前，显示的图片 R.mipmap.img_default_yuan
                .fallback(fallback) //url为空的时候,显示的图片
                .error(error);//图片加载失败后，显示的图片 .centerCrop();


        return requestOptions;
    }

}
