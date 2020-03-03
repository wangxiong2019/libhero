package com.hero.libhero.mydialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hero.libhero.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/5.
 */
public class MyDialogList extends Dialog {


    String TAG = "MyDialogList";
    protected Context mContext;// mContext(上下文)

    protected DisplayMetrics mDisplayMetrics;// (DisplayMetrics)设备密度
    protected Window window;

    public MyDialogList(Context context, TextView textView) {
        super(context);
        mContext = context;
        this.textView = textView;
        this.posStr = textView.getText().toString();
        init();
    }


    public void init() {
        windowStyle();
    }

    public void windowStyle() {


        window = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);// android:windowNoTitle
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// android:windowBackground
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// android:backgroundDimEnabled默认是true的
        window.setWindowAnimations(R.style.mystyle_left_right);

        show();
    }


    protected float mWidthScale = 0.8f;//(宽度比例)
    protected float mHeightScale = 0.85f;//(高度比例)

    protected float mMaxHeight;//(最大高度)

    protected LinearLayout mLlTop;//(最上层容器)
    protected LinearLayout mLlControlHeight;//(用于控制对话框高度)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");

        mDisplayMetrics = mContext.getResources().getDisplayMetrics();
        mMaxHeight = mDisplayMetrics.heightPixels - getHeight(mContext);
        onCreateView();
        setContentView(mLlTop, new ViewGroup.LayoutParams(mDisplayMetrics.widthPixels, (int) mMaxHeight));
    }


    /**
     * container
     */
    protected LinearLayout mLlContainer;


    protected TextView mTvTitle;//(标题)
    protected String mTitle;
    protected int mTitleTextColor = Color.parseColor("#ffffff");//(标题颜色)
    private int mTitleBgColor = Color.parseColor("#fa6a3d");
    protected float mTitleTextSize = 15f;//(标题字体大小,单位sp)

    private ListView mLv;
    private int mLvBgColor = Color.parseColor("#ffffff");

    private void onCreateView() {
        Log.e(TAG, "onCreateView-----");
        mLlTop = new LinearLayout(mContext);
        mLlTop.setGravity(Gravity.CENTER);
        mLlTop.setOrientation(LinearLayout.VERTICAL);

        mLlContainer = new LinearLayout(mContext);
        mLlContainer.setOrientation(LinearLayout.VERTICAL);
        mLlContainer.setGravity(Gravity.CENTER);

        mTvTitle = new TextView(mContext);
        mTvTitle.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mLlContainer.addView(mTvTitle);

        /** listview */
        mLv = new ListView(mContext);
        mLv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mLv.setCacheColorHint(Color.TRANSPARENT);
        mLv.setFadingEdgeLength(0);
        mLv.setVerticalScrollBarEnabled(false);
        mLv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mLlContainer.addView(mLv);

        mLlControlHeight = new LinearLayout(mContext);
        mLlControlHeight.setOrientation(LinearLayout.VERTICAL);
        mLlControlHeight.setGravity(Gravity.CENTER);
        mLlControlHeight.addView(mLlContainer);

        mLlTop.addView(mLlControlHeight);
    }

    private BaseAdapter mAdapter;//适配器
    private List<String> mContents;//数据源
    private LayoutAnimationController mLac;
    private TextView textView;
    private String posStr;

    //赋值
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        Log.e(TAG, "onAttachedToWindow-----");

        float radius = dp2px(mCornerRadius);

        int width = (int) (mDisplayMetrics.widthPixels * mWidthScale);
        int height = (int) (mMaxHeight * mHeightScale);
        Log.e(TAG, "onAttachedToWindow-----"+width+"---"+height);

        mLlControlHeight.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        /** title */

        mTvTitle.setMinHeight(dp2px(48));
        mTvTitle.setGravity(Gravity.CENTER);
       // mTvTitle.setPadding(dp2px(15), dp2px(5), dp2px(0), dp2px(5));
        mTvTitle.setVisibility(View.VISIBLE);
        mTvTitle.setTextSize(mTitleTextSize);
        mTvTitle.setText(mTitle);
        mTvTitle.setTextColor(mTitleTextColor);

        mTvTitle.setBackgroundDrawable(cornerDrawable(
                mTitleBgColor, new float[]{radius, radius, radius,radius, 0, 0, 0, 0}));
        mLv.setBackgroundDrawable(cornerDrawable(
                mLvBgColor,new float[]{ 0, 0, 0, 0,radius, radius, radius,radius}));

        if (mAdapter == null) {
            mAdapter = new ListDialogAdapter();
        }
        if (mContents.size() == 0) {
            mContents = new ArrayList<>();
            mContents.add("无数据");
        }
        mLv.setAdapter(mAdapter);
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String str = mContents.get(position);
                if (!str.equals("无数据")) {
                    if (mHandler != null) {
                        if (null != textView) {
                            textView.setText(str);
                        }
                        Message msg = new Message();
                        msg.obj = str;
                        msg.what = 500;
                        mHandler.sendMessageDelayed(msg, 500);
                    } else {
                        if (null != textView) {
                            textView.setText(str);
                        }
                        posStr = str;
                    }
                } else {
                    textView.setText("");
                    posStr = "";
                }
                dismiss();
            }
        });
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        dismiss();
                        return true;
                    default:
                        return false;
                }
            }
        });

//        setCancelable(false);
        setCanceledOnTouchOutside(false);


    }

    private Handler mHandler;

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public List<String> getmContents() {
        return mContents;
    }

    public void setmContents(List<String> mContents) {
        this.mContents = mContents;
    }

    public Handler getmHandler() {
        return mHandler;
    }

    public void setmHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    //item
    private int mItemTextColor = Color.parseColor("#303030");
    private int mItemPressColor = Color.parseColor("#bababa");
    private float mItemTextSize = 15f;
    private float mCornerRadius = 5;

    private class ListDialogAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mContents.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final String item = mContents.get(position);

            int left = dp2px(10);
            int top = dp2px(10);
            int right = dp2px(10);
            int bottom = dp2px(10);

            LinearLayout llItem = new LinearLayout(mContext);
            llItem.setOrientation(LinearLayout.HORIZONTAL);
            llItem.setGravity(Gravity.CENTER_VERTICAL);

            ImageView img = new ImageView(mContext);
            img.setLayoutParams(new LinearLayout.LayoutParams(left, left));
            img.setImageResource(R.drawable.radiobox_om);


            llItem.addView(img);

            TextView tvItem = new TextView(mContext);
            tvItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            tvItem.setSingleLine(true);
            tvItem.setTextColor(mItemTextColor);
            tvItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, mItemTextSize);
            tvItem.setPadding(left, 0, 0, 0);
            llItem.addView(tvItem);
            float radius = dp2px(mCornerRadius);

            llItem.setBackgroundDrawable(listItemSelector(radius, Color.TRANSPARENT, mItemPressColor,
                    mContents.size(), position));


            llItem.setPadding(left, top, right, bottom);

            tvItem.setText(item);
            img.setVisibility(!item.equals(posStr) ? View.INVISIBLE : View.VISIBLE);

            return llItem;
        }
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
        String sysVersion = android.os.Build.VERSION.RELEASE;
        if ("4.4.4".equals(sysVersion)) {
            String sysIncrement = android.os.Build.VERSION.INCREMENTAL;
            String displayId = android.os.Build.DISPLAY;
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
    /**
     * set ListView item selector with corner drawable for the first and the last position
     * (ListView的item点击效果,第一项和最后一项圆角处理)
     */
    public static StateListDrawable listItemSelector(float radius, int normalColor, int pressColor, int itemTotalSize,
                                                     int itemPosition) {
        StateListDrawable bg = new StateListDrawable();
        Drawable normal = null;
        Drawable pressed = null;

        if (itemPosition == 0 && itemPosition == itemTotalSize - 1) {// 只有一项
            normal = cornerDrawable(normalColor, new float[]{radius, radius, radius, radius, radius, radius, radius,
                    radius});
            pressed = cornerDrawable(pressColor, new float[]{radius, radius, radius, radius, radius, radius, radius,
                    radius});
        }
//        else if (itemPosition == 0) {
//            normal = cornerDrawable(normalColor, new float[]{radius, radius, radius, radius, 0, 0, 0, 0,});
//            pressed = cornerDrawable(pressColor, new float[]{radius, radius, radius, radius, 0, 0, 0, 0});
//        }
        else if (itemPosition == itemTotalSize - 1) {
            normal = cornerDrawable(normalColor, new float[]{0, 0, 0, 0, radius, radius, radius, radius});
            pressed = cornerDrawable(pressColor, new float[]{0, 0, 0, 0, radius, radius, radius, radius});
        } else {
            normal = new ColorDrawable(normalColor);
            pressed = new ColorDrawable(pressColor);
        }

        bg.addState(new int[]{-android.R.attr.state_pressed}, normal);
        bg.addState(new int[]{android.R.attr.state_pressed}, pressed);
        return bg;
    }
}
