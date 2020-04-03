package com.hero.libhero.base;

/**
 * 创建 by hero
 * 时间 2020/3/20
 * 类名 自定义监听器
 */
public interface RvAdapterListener {

    // 当Cell点击的时候触发
    void onItemClick(int pos);

    // 当Cell长按时触发
    boolean onItemLongClick(int pos);
}
