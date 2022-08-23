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
        SessionManager.setUser(user)
        var cacheUser = SessionManager.getUser<MyUserInfo>()
        SessionManager.put("a1", "StringValue")
        SessionManager.put("a2", 10086)
        SessionManager.put("a3", true)
        SessionManager.put("a4", 3.14f)
        SessionManager.put("a5", 360L)
        SessionManager.put("a6", user)
        Log.d("Rae", "当前用户：$cacheUser")
//        SessionManager.default.forgot()
        cacheUser = SessionManager.getUser<MyUserInfo>()
        Log.d("Rae", "退出登录用户：$cacheUser")

    }
}

data class MyUserInfo(
    var account: String,
    var password: String,
    var nickname: String,
)