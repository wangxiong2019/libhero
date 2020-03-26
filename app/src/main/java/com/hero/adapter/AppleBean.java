package com.hero.adapter;

import java.math.BigDecimal;

/**
 * 创建 by hero
 * 时间 2020/3/26
 * 类名 model类
 */
public class AppleBean {

    private Integer id;
    private String name;
    private BigDecimal money;
    private Integer num;
    public AppleBean(Integer id, String name, BigDecimal money, Integer num) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.num = num;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
