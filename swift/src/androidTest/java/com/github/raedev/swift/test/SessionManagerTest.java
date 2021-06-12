package com.github.raedev.swift.test;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.github.raedev.swift.entity.UserInfo;
import com.github.raedev.swift.session.SessionManager;
import com.github.raedev.swift.session.SessionStateListener;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SessionManagerTest {

    private Context getContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void testSessionUserInfo() {
        SessionManager.initDefaultSessionManager(getContext(), UserInfo.class);
        SessionManager manager = SessionManager.getDefault();
        manager.addSessionStateListener(new SessionStateListener() {
            @Override
            public void onUserInfoChanged(SessionManager sessionManager) {
                Log.i("rae", "用户信息改变了:" + sessionManager.getUserInfo());
            }
        });
        manager.setUserInfo(new UserInfo("RAE"));
        manager.setUserInfo(new UserInfo("LONG"));
        UserInfo info = manager.getUserInfo();
        Log.d("Rae", info == null ? "为空" : "获取到用户信息：" + info.toString());
    }


}