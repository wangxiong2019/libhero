package com.hero.libhero.imageview.preview.enitity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Parcel;


import androidx.annotation.Nullable;


@SuppressLint("ParcelCreator")
public class ImageViewInfo implements IPreviewInfo {
    private String mUrl;  //图片地址
    private Rect mBounds; // 记录坐标
    private String mVideoUrl;

    private String mDescription = "描述信息";

    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public ImageViewInfo(String mUrl, Bitmap bitmap) {
        this.mUrl = mUrl;
        this.bitmap = bitmap;
    }

    public ImageViewInfo(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ImageViewInfo(String url) {
        mUrl = url;
    }

    public ImageViewInfo(String videoUrl, String url) {
        mUrl = url;
        mVideoUrl = videoUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @Override
    public String getUrl() {//将你的图片地址字段返回
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public Rect getBounds() {//将你的图片显示坐标字段返回
        return mBounds;
    }

    @Nullable
    @Override
    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setBounds(Rect bounds) {
        mBounds = bounds;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUrl);
        dest.writeParcelable(mBounds, flags);
        dest.writeString(mDescription);
        dest.writeString(mVideoUrl);

    }

    protected ImageViewInfo(Parcel in) {
        mUrl = in.readString();
        mBounds = in.readParcelable(Rect.class.getClassLoader());
        mDescription = in.readString();
        mVideoUrl = in.readString();
    }

    public static final Creator<ImageViewInfo> CREATOR = new Creator<ImageViewInfo>() {
        @Override
        public ImageViewInfo createFromParcel(Parcel source) {
            return new ImageViewInfo(source);
        }

        @Override
        public ImageViewInfo[] newArray(int size) {
            return new ImageViewInfo[size];
        }
    };
}
