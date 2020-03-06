package com.hero.libhero.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by Administrator on 2016/4/7.
 */
public class AnimatorUtil {

    static String TAG = "AnimatorUtil";

    /**
     * 方法: ShowAnimator 动画出现的高度
     */
    public static ValueAnimator createHeightAnimator(final View view, final int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                //Log.e(TAG,"ShowAnimator-value="+value);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public static void ShowAnimator(final View view) {
        view.setVisibility(View.VISIBLE);
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);
        int height = view.getMeasuredHeight();
        ValueAnimator animator = createHeightAnimator(view, 0, height);
        animator.setDuration(500);
        animator.start();
        Log.e(TAG, "ShowAnimator-height=" + height);


//        TranslateAnimation    mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
//        mShowAction.setDuration(500);
//
//        view.startAnimation(mShowAction);
//        view.setVisibility(View.VISIBLE);
    }


    /**
     * 方法: animateCollapsing <p>
     * 描述: 收缩动画 <p>
     */
    public static void HideAnimator(final View view) {
        int height2 = view.getHeight();
        Log.e(TAG, "HideAnimator-height2=" + height2);
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);
        int height = view.getMeasuredHeight();
        Log.e(TAG, "HideAnimator-height=" + height);
        ValueAnimator animator = createHeightAnimator(view, height2, 0);
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
            }
        });
        animator.setDuration(1000);
        animator.start();
    }
    //定义从右侧进入的动画效果
    public static Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    //定义从左侧进入的动画效果
    public static Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(500);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }


    //旋转
    public static void RotateAnimation(View mView, Handler mCountHandler) {
        final float centerX = mView.getWidth() / 2.0f;
        final float centerY = mView.getHeight() / 2.0f;
        RotateAnimation rotateAnimation = new RotateAnimation(0, 180, centerX, centerY);
        //第一个参数表示动画的起始角度，第二个参数表示动画的结束角度，第三个表示动画的旋转中心x轴，第四个表示动画旋转中心y轴。
        rotateAnimation.setDuration(1000 * 1);
        rotateAnimation.setFillAfter(false);//ture表示动画结束后停留在动画的最后位置，false表示动画结束后回到初始位置，默认为false。

        mView.startAnimation(rotateAnimation);

        mCountHandler.sendEmptyMessageDelayed(0, 1000);
    }

    //顺时针旋转180
    public static void RotateAnimation180Shun(View mView) {
        final float centerX = mView.getWidth() / 2.0f;
        final float centerY = mView.getHeight() / 2.0f;
        RotateAnimation rotateAnimation = new RotateAnimation(0, 180, centerX, centerY);
        //第一个参数表示动画的起始角度，第二个参数表示动画的结束角度，第三个表示动画的旋转中心x轴，第四个表示动画旋转中心y轴。
        rotateAnimation.setDuration(1000 * 1);
        rotateAnimation.setFillAfter(true);//ture表示动画结束后停留在动画的最后位置，false表示动画结束后回到初始位置，默认为false。

        mView.startAnimation(rotateAnimation);
    }
    //逆时针旋转180
    public static void RotateAnimation180Ni(View mView) {
        final float centerX = mView.getWidth() / 2.0f;
        final float centerY = mView.getHeight() / 2.0f;
        RotateAnimation rotateAnimation = new RotateAnimation(0, 180, centerX, centerY);
        //第一个参数表示动画的起始角度，第二个参数表示动画的结束角度，第三个表示动画的旋转中心x轴，第四个表示动画旋转中心y轴。
        rotateAnimation.setDuration(1000 * 1);
        rotateAnimation.setFillAfter(true);//ture表示动画结束后停留在动画的最后位置，false表示动画结束后回到初始位置，默认为false。

        mView.startAnimation(rotateAnimation);
    }
    //旋转
    public static void RotateAnimation360(View mView) {
        final float centerX = mView.getWidth() / 2.0f;
        final float centerY = mView.getHeight() / 2.0f;
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, centerX, centerY);
        //第一个参数表示动画的起始角度，第二个参数表示动画的结束角度，第三个表示动画的旋转中心x轴，第四个表示动画旋转中心y轴。
        rotateAnimation.setDuration(1000 * 1);
        rotateAnimation.setFillAfter(true);//ture表示动画结束后停留在动画的最后位置，false表示动画结束后回到初始位置，默认为false。

        mView.startAnimation(rotateAnimation);
    }
}
