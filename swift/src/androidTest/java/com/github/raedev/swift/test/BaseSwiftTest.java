package com.github.raedev.swift.test;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.github.raedev.swift.AppSwift;

import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * Created by RAE on 2020/10/17.
 */
@RunWith(AndroidJUnit4.class)
public abstract class BaseSwiftTest {

    private Application mApplication;

    protected Context getContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    protected Application getApplication() {
        if (mApplication == null) {
            mApplication = (Application) InstrumentationRegistry
                    .getInstrumentation()
                    .getTargetContext()
                    .getApplicationContext();
        }
        return mApplication;
    }

    @Before
    public void setup() {
        // 初始化
        AppSwift.init(getApplication());
    }

    protected static void log(String message, Object... args) {
        Log.d("rae", String.format(message, args));
    }
}
