package com.lcm.test.redistest.pojo;

import lombok.Data;

import java.util.*;

@Data
public class User {

    private String userId;

    private String username;

    private String password;

    private String mobileNum;

    private String companyAbbr;

    private Date createTime;

    private Date updateTime;

    public User() {
        userId = UUID.randomUUID().toString();
        username="test";
        password="test";
        mobileNum="test";
        companyAbbr="test";
        createTime=new Date();
        updateTime=createTime;
    }

    public static List<User> createUser(int num){
        List<User> list=new ArrayList<>(num);
        for(int i=0;i<num;i++){
            User user=new User();
            list.add(user);
        }
        return list;
    }
}