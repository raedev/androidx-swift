package com.github.raedev.swift.entity;

import com.github.raedev.swift.annotation.ConfigClass;

/**
 * Created by RAE on 2020/10/17.
 */
@ConfigClass("myConfig")
public interface MyConfig {

    void setName(String name);

    String getName();
}
