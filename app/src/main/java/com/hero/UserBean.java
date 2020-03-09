package com.hero;


import android.os.Parcel;
import android.os.Parcelable;

import com.hero.libhero.mydb.dbinit.annotation.Column;
import com.hero.libhero.mydb.dbinit.annotation.Table;

@Table(name = "UserBean")
public class UserBean {

    //  主键
    @Column(name = "id", isId = true)
    private long id;

    @Column(name = "name")
    private String name = "张三";

    @Column(name = "age")
    private int age = 18;

    @Column(name = "sex")
    private String sex;

//    public UserBean(long id, String name, int age, String sex) {
//        this.id = id;
//        this.name = name;
//        this.age = age;
//        this.sex = sex;
//    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }





}
