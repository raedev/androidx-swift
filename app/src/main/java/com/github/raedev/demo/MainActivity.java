package com.github.raedev.demo;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.raedev.swift.session.SessionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化
        SessionManager.init(new SessionManager.Builder(this).setUserClass(UserBean.class).setJsonMode(true));
        SessionManager manager = SessionManager.getDefault();
        Log.d("rae", "是否登录：" + manager.isLogin());
        Log.d("rae", "用户信息：" + manager.getUserInfo());
        manager.setUserInfo(new UserBean());
        Log.d("rae", "新用户信息：" + manager.getUserInfo());

    }

    protected class UserBean {
        public String name = "测试";
        public String age = "12";


        @Override
        public String toString() {
            return "UserBean{" +
                    "name='" + name + '\'' +
                    ", age='" + age + '\'' +
                    '}';
        }
    }
}