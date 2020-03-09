package com.hero;

//EventBus    2.定义事件
public class WxCodeEB {
    private String code;

    public WxCodeEB(String code){
        this.code=code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
