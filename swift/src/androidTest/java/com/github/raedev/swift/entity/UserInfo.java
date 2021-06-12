package com.github.raedev.swift.entity;

import javax.annotation.Nonnull;

/**
 * Created by RAE on 2020/10/17.
 */
public class UserInfo {
    private String name;
    private int age;

    public UserInfo() {
    }

    public UserInfo(String name) {
        this.name = name;
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

    @Override
    @Nonnull
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
