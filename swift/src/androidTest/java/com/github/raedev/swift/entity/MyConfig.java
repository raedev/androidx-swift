package com.github.raedev.swift.entity;

import com.github.raedev.swift.annotation.Configuration;

/**
 * Created by RAE on 2020/10/17.
 */
@Configuration("myConfig")
public interface MyConfig {

    void setName(String name);

    String getName();
}
