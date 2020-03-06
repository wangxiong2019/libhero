package com.hero.libhero.okhttp.https;

public interface MyCallBack {
    void failBack(String res_msg, int res_code);
    void successBack(String res_data);
}
