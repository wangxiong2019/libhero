package com.hero.libhero.permissions;

import androidx.annotation.NonNull;

/**
 * 创建 by hero
 * 时间 2020/4/15
 * 类名
 */
public interface PermissionListener {
    /**
     * 通过授权
     * @param permission
     */
    void permissionGranted(@NonNull String[] permission);

    /**
     * 拒绝授权
     * @param permission
     */
    void permissionDenied(@NonNull String[] permission);
}
