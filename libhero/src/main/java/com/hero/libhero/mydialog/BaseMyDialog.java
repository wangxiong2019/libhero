package com.hero.libhero.mydialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hero.libhero.R;


/**
 * Created by Administrator on 2017/8/5.
 */
public abstract class BaseMyDialog extends Dialog {



    String TAG = "BaseMyDialog";
    protected Context mContext;// mContext(上下文)

    protected DisplayMetrics mDisplayMetrics;// (DisplayMetrics)设备密度
    protected Window window;

    public BaseMyDialog(Context context) {
        super(context);
        mContext = context;
        init();
    }



    public abstract   void init() ;

    public void windowStyle(){


        window = getWindow();
        //requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// android:windowBackground
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// android:backgroundDimEnabled默认是true的
        window.setWindowAnimations(R.style.mystyle_left_right);

        //show();
    }

    public void windowStyleWithType(){



        window = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// android:windowBackground
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// android:backgroundDimEnabled默认是true的
        window.setWindowAnimations(R.style.mystyle_left_right);

        //TYPE_APPLICATION_OVERLAY
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.O) {//8.0及以上
            window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else if (sdkInt >= Build.VERSION_CODES.M) {//6.0-7.0
            window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }

        window.setFormat(PixelFormat.TRANSLUCENT);

       // show();
    }

    protected LinearLayout mLlTop;//(最上层容器)
    protected LinearLayout mLlControlHeight;//(用于控制对话框高度)
    protected float mMaxHeight;//(最大高度)
    protected View mOnCreateView;//(创建出来的mLlControlHeight的直接子View)

    protected float mWidthScale = 0.75f;//(宽度比例)
    protected float mHeightScale;//(高度比例)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");

        mDisplayMetrics = mContext.getResources().getDisplayMetrics();
        mMaxHeight = mDisplayMetrics.heightPixels - getHeight(mContext);
        onCreateView();
        setContentView(mLlTop, new ViewGroup.LayoutParams(mDisplayMetrics.widthPixels, (int) mMaxHeight));
    }


    //////////////////////////////////

    /**
     * container
     */
    protected LinearLayout mLlContainer;

    /**
     * 标题
     */
    protected TextView mTvTitle;//(标题)
    protected String mTitle;
    protected int mTitleTextColor = Color.parseColor("#61AEDC");//(标题颜色)
    protected float mTitleTextSize = 15f;//(标题字体大小,单位sp)
    protected boolean mIsTitleShow = false;//(是否显示标题)


    private View mVLineTitle;//标题下划线
    private int mTitleLineColor = Color.parseColor("#61AEDC");//(标题下划线颜色)
    private float mTitleLineHeight = 0f;//(标题下划线高度)

    /**
     * content
     */

    protected TextView mTvContent;//(内容)
    protected String mContent;
    protected int mContentGravity = Gravity.CENTER;//(正文内容显示位置)
    protected int mContentTextColor = Color.parseColor("#666666");//(正文字体颜色)
    protected float mContentTextSize = 14f;//(正文字体大小)


    /**
     * btns
     */
    protected LinearLayout mLlBtns;//装载按钮的线性布局
    protected TextView mTvBtnLeft;//取消按钮
    protected TextView mTvBtnRight;//确定按钮

    protected String mBtnLeftText = "关闭";
    protected String mBtnRightText = "确定";

    protected int mLeftBtnTextColor = Color.parseColor("#1e90ff");
    protected int mRightBtnTextColor = Color.parseColor("#1e90ff");

    private View mVLineHorizontal;//按钮上划线
    private View mVLineVertical;//两个按钮之间的垂直线

    private int mDividerColor = Color.parseColor("#DCDCDC");//划线 颜色

    protected float mCornerRadius = 5;//(圆角程度,单位dp)
    protected int mBgColor = Color.parseColor("#ffffff");//(背景颜色)
    protected int mBtnPressColor = Color.parseColor("#E3E3E3");// (按钮点击颜色)

    //创建view
    public void onCreateView() {
        Log.e(TAG, "onCreateView-----");
        mLlTop = new LinearLayout(mContext);
        mLlTop.setGravity(Gravity.CENTER);

        mLlContainer = new LinearLayout(mContext);
        mLlContainer.setOrientation(LinearLayout.VERTICAL);
        mLlContainer.setGravity(Gravity.CENTER);

        mTvTitle = new TextView(mContext);
        mTvTitle.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mLlContainer.addView(mTvTitle);

        /** title underline
         * 标题下划线
         */
        mVLineTitle = new View(mContext);
        mLlContainer.addView(mVLineTitle);

        /** content */
        mTvContent = new TextView(mContext);
        mTvContent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mTvContent.setTop(50);
        mTvContent.setBottom(50);
        mTvContent.setMinHeight(300);
        mLlContainer.addView(mTvContent);

        mVLineHorizontal = new View(mContext);
        mVLineHorizontal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        mLlContainer.addView(mVLineHorizontal);

        /** btns */

        mLlBtns = new LinearLayout(mContext);
        mLlBtns.setOrientation(LinearLayout.HORIZONTAL);

        mTvBtnLeft = new TextView(mContext);
        mTvBtnLeft.setGravity(Gravity.CENTER);

        mTvBtnRight = new TextView(mContext);
        mTvBtnRight.setGravity(Gravity.CENTER);


        mTvBtnLeft.setLayoutParams(new LinearLayout.LayoutParams(0, dp2px(45), 1));
        mLlBtns.addView(mTvBtnLeft);

        mVLineVertical = new View(mContext);
        mVLineVertical.setLayoutParams(new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT));
        mLlBtns.addView(mVLineVertical);

        mTvBtnRight.setLayoutParams(new LinearLayout.LayoutParams(0, dp2px(45), 1));
        mLlBtns.addView(mTvBtnRight);

        mLlContainer.addView(mLlBtns);


        mLlControlHeight = new LinearLayout(mContext);
        mLlControlHeight.setOrientation(LinearLayout.VERTICAL);
        mLlControlHeight.setGravity(Gravity.CENTER);
        mLlControlHeight.addView(mLlContainer);


        mLlTop.addView(mLlControlHeight);


    }


    private boolean mTvBtnLeftDismiss=false;

    public boolean mTvBtnLeftDismiss() {
        return mTvBtnLeftDismiss;
    }

    public void setmTvBtnLeftDismiss(boolean mTvBtnLeftDismiss) {
        this.mTvBtnLeftDismiss = mTvBtnLeftDismiss;
    }

    //赋值
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        Log.e(TAG, "onAttachedToWindow-----");


        int width = (int) (mDisplayMetrics.widthPixels * mWidthScale);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;

        mLlControlHeight.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        /** title */

        mTvTitle.setMinHeight(dp2px(48));
        mTvTitle.setGravity(Gravity.CENTER);
        mTvTitle.setPadding(dp2px(15), dp2px(5), dp2px(0), dp2px(5));
        mTvTitle.setVisibility(mIsTitleShow ? View.VISIBLE : View.GONE);
        mTvTitle.setTextSize(mTitleTextSize);
        mTvTitle.setText(mTitle);

        /** title underline */
        mVLineTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                dp2px(mTitleLineHeight)));
        mVLineTitle.setBackgroundColor(mTitleLineColor);
        mVLineTitle.setVisibility(mIsTitleShow ? View.VISIBLE : View.GONE);

        /** content */
        mTvContent.setPadding(dp2px(15), dp2px(15), dp2px(15), dp2px(15));
        mTvContent.setMinHeight(dp2px(100));
        mTvContent.setGravity(Gravity.CENTER);

        mTvContent.setGravity(mContentGravity);
        mTvContent.setText(mContent);
        mTvContent.setTextColor(mContentTextColor);
        mTvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, mContentTextSize);
        mTvContent.setLineSpacing(0, 1.4f);

        /** btns */
        mVLineHorizontal.setBackgroundColor(mDividerColor);
        mVLineVertical.setBackgroundColor(mDividerColor);

        float radius = dp2px(mCornerRadius);
        mLlContainer.setBackgroundDrawable(cornerDrawable(mBgColor, radius));

        mTvBtnLeft.setBackgroundDrawable(btnSelector(radius, mBgColor, mBtnPressColor, 0));

        if(mTvBtnLeftDismiss){//只留一个按钮
            mTvBtnLeft.setVisibility(View.GONE);
            mVLineVertical.setVisibility(View.GONE);
            mTvBtnRight.setBackgroundDrawable(btnSelector(radius, mBgColor, mBtnPressColor, -1));
        }else {
            mTvBtnRight.setBackgroundDrawable(btnSelector(radius, mBgColor, mBtnPressColor, 1));
        }




        mTvBtnLeft.setText(mBtnLeftText);
        mTvBtnRight.setText(mBtnRightText);

        mTvBtnLeft.setTextColor(mLeftBtnTextColor);
        mTvBtnRight.setTextColor(mRightBtnTextColor);

        mTvBtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelClick != null) {
                    cancelClick.doCancel();
                } else {
                    dismiss();
                }
            }
        });

        mTvBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okClick != null) {
                    okClick.doOk();
                } else {
                    dismiss();
                }
            }
        });

//        setOnKeyListener(new OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                switch (keyCode) {
//                    case KeyEvent.KEYCODE_BACK:
//                        dismiss();
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//        });

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    private DoCancel cancelClick;//(左按钮接口)
    private DoOk okClick;//(右按钮接口)





    public boolean ismIsTitleShow() {
        return mIsTitleShow;
    }

    public void setmIsTitleShow(boolean mIsTitleShow) {
        this.mIsTitleShow = mIsTitleShow;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }


    public float getmWidthScale() {
        return mWidthScale;
    }

    public void setmWidthScale(float mWidthScale) {
        this.mWidthScale = mWidthScale;
    }

    public float getmHeightScale() {
        return mHeightScale;
    }

    public void setmHeightScale(float mHeightScale) {
        this.mHeightScale = mHeightScale;
    }

    public String getmBtnLeftText() {
        return mBtnLeftText;
    }

    public void setmBtnLeftText(String mBtnLeftText) {
        this.mBtnLeftText = mBtnLeftText;
    }

    public String getmBtnRightText() {
        return mBtnRightText;
    }

    public void setmBtnRightText(String mBtnRightText) {
        this.mBtnRightText = mBtnRightText;
    }

    public DoCancel getcancelClick() {
        return cancelClick;
    }

    public void setcancelClick(DoCancel cancelClick) {
        this.cancelClick = cancelClick;
    }

    public DoOk getokClick() {
        return okClick;
    }

    public void setokClick(DoOk okClick) {
        this.okClick = okClick;
    }




    public static int getHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        if (isFlymeOs4x()) {
            return 2 * statusBarHeight;
        }

        return statusBarHeight;
    }

    public static boolean isFlymeOs4x() {
        String sysVersion = Build.VERSION.RELEASE;
        if ("4.4.4".equals(sysVersion)) {
            String sysIncrement = Build.VERSION.INCREMENTAL;
            String displayId = Build.DISPLAY;
            if (!TextUtils.isEmpty(sysIncrement)) {
                return sysIncrement.contains("Flyme_OS_4");
            } else {
                return displayId.contains("Flyme OS 4");
            }
        }
        return false;
    }

    /**
     * dp to px
     */
    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    /**
     * set btn selector with corner drawable for special position
     */
    public static StateListDrawable btnSelector(float radius, int normalColor, int pressColor, int postion) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = null;
        Drawable pressed = null;

        if (postion == 0) {// left btn
            normal = cornerDrawable(normalColor, new float[]{0, 0, 0, 0, 0, 0, radius, radius});
            pressed = cornerDrawable(pressColor, new float[]{0, 0, 0, 0, 0, 0, radius, radius});
        } else if (postion == 1) {// right btn
            normal = cornerDrawable(normalColor, new float[]{0, 0, 0, 0, radius, radius, 0, 0});
            pressed = cornerDrawable(pressColor, new float[]{0, 0, 0, 0, radius, radius, 0, 0});
        } else if (postion == -1) {// only one btn
            normal = cornerDrawable(normalColor, new float[]{0, 0, 0, 0, radius, radius, radius, radius});
            pressed = cornerDrawable(pressColor, new float[]{0, 0, 0, 0, radius, radius, radius, radius});
        }

        bg.addState(new int[]{-android.R.attr.state_pressed}, normal);
        bg.addState(new int[]{android.R.attr.state_pressed}, pressed);
        return bg;
    }

    public static Drawable cornerDrawable(final int bgColor, float cornerradius) {
        final GradientDrawable bg = new GradientDrawable();
        bg.setCornerRadius(cornerradius);
        bg.setColor(bgColor);

        return bg;
    }

    public static Drawable cornerDrawable(final int bgColor, float[] cornerradius) {
        final GradientDrawable bg = new GradientDrawable();
        bg.setCornerRadii(cornerradius);
        bg.setColor(bgColor);

        return bg;
    }
}
