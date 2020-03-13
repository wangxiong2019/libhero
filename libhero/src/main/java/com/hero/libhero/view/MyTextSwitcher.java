package com.hero.libhero.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * 创建 by hero
 * 时间 2020/3/13
 * 类名 改变字颜色
 */
public class MyTextSwitcher extends ViewSwitcher {
    /**
     * Creates a new empty TextSwitcher.
     *
     * @param context the application's environment
     */
    public MyTextSwitcher(Context context) {
        super(context);
    }

    /**
     * Creates a new empty TextSwitcher for the given context and with the
     * specified set attributes.
     *
     * @param context the application environment
     * @param attrs   a collection of attributes
     */
    public MyTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if child is not an instance of
     *                                  {@link android.widget.TextView}
     */
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (!(child instanceof TextView)) {
            throw new IllegalArgumentException(
                    "TextSwitcher children must be instances of TextView");
        }

        super.addView(child, index, params);
    }

    /**
     * Sets the text of the next view and switches to the next view. This can
     * be used to animate the old text out and animate the next text in.
     *
     * @param text the new text to display
     */
    public void setText(CharSequence text,int colorId) {
        final TextView t = (TextView) getNextView();
        t.setText(text);
        t.setTextColor(colorId);
        showNext();
    }

    /**
     * Sets the text of the text view that is currently showing.  This does
     * not perform the animations.
     *
     * @param text the new text to display
     */
    public void setCurrentText(CharSequence text) {
        ((TextView) getCurrentView()).setText(text);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return TextSwitcher.class.getName();
    }
}