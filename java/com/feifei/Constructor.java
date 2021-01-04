package com.feifei;

import java.util.ArrayList;

/**
 * Description:
 *
 * @ClassName: Constructor
 * @Author chengfei
 * @Date 2020/12/31 16:36
 **/
public class Constructor {
    public String sex ;
    public Constructor(String sex){
        this.sex = sex;
    }

    public static void main(String[] args) {
        Constructor constructor = new Constructor("女");
        System.out.println(constructor.sex);
    }
}
