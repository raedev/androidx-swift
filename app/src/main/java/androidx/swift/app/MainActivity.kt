package androidx.swift.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.swift.core.AppConfigManager
import androidx.swift.session.SessionManager
import androidx.swift.sword.SdkObserver
import androidx.swift.sword.paging.PageObservable

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SessionManager.setDefault(
            SessionManager.Builder(
                this.applicationContext,
                UserInfo::class.java
            ).build()
        )

        var user = SessionManager.getDefault().getUser<UserInfo>()
        Log.d("Rae", "缓存用户$user")
        Log.d("rae", "自定义值：${SessionManager.getDefault().getFloat("isFloat", 1f)}")
        Log.d("rae", "Object：${SessionManager.getDefault().get("isObject", UserInfo::class.java)}")
        user = UserInfo("Rae", "123")

        SessionManager.getDefault().setUser(user)
        Log.d("Rae", "设置用户$user")

        SessionManager.getDefault().put("isBoolean", true)
        SessionManager.getDefault().put("isString", "rae is a boy")
        SessionManager.getDefault().put("isInt", 1314)
        SessionManager.getDefault().put("isFloat", 3.14f)
        SessionManager.getDefault().put("isLong", 3000L)
        SessionManager.getDefault().put("isObject", UserInfo("newObject", "111"))

        val appConfig = AppConfigManager.getConfig(AppConfig::class)

        println(appConfig.appName)

        val config = appConfig.apply {
            appName = "swift"
            version = 12
            isLogin = true
            age = 30f
            userInfo = user
        }

        Log.i("Rae", "名称：${config}")
        Log.i("Rae", "配置用户：${config.userInfo?.name}")

    }
}

interface AppConfig {
    var appName: String
    var version: Int
    var isLogin: Boolean
    var age: Float

    var userInfo: UserInfo?

    fun userAccount(): String {
        return userInfo?.name ?: "guest"
    }
}

data class UserInfo(
    var name: String,
    var password: String
)