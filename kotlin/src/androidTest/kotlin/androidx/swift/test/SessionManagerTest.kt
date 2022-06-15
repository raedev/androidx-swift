package androidx.swift.test

import android.util.Log
import androidx.swift.session.SessionManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author RAE
 * @date 2022/06/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */

@RunWith(AndroidJUnit4::class)
class SessionManagerTest {

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // 初始化
        SessionManager.Builder(context, MyUserInfo::class.java).build()
    }

    @Test
    fun testSession() {
        val user = MyUserInfo("rae", "123456", "陈睿")
        SessionManager.default.setUser(user)
        var cacheUser = SessionManager.default.getUser<MyUserInfo>()
        SessionManager.default.put("a1", "StringValue")
        SessionManager.default.put("a2", 10086)
        SessionManager.default.put("a3", true)
        SessionManager.default.put("a4", 3.14f)
        SessionManager.default.put("a5", 360L)
        SessionManager.default.put("a6", user)
        Log.d("Rae", "当前用户：$cacheUser")
//        SessionManager.default.forgot()
        cacheUser = SessionManager.default.getUser<MyUserInfo>()
        Log.d("Rae", "退出登录用户：$cacheUser")

    }
}

data class MyUserInfo(
    var account: String,
    var password: String,
    var nickname: String,
)