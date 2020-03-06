package com.hero.libhero.okhttp.https;


public interface ReqProgressCallBack extends MyCallBack {

    /**
     * 响应进度更新
     */
    void progressBack(long total, long current);
}
