package com.cicinnus.cateye.tools;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;

/**
 * Created by Work on 2017/6/10.
 */

public class Person extends BmobObject{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String name;
    private String password;

    public Person(String name, String password) {
        this.name = name;
        this.password = password;
    }

}
