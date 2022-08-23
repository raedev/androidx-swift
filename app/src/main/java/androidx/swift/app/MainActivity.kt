package androidx.swift.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.swift.AndroidSwift
import androidx.swift.core.AppConfigManager
import androidx.swift.session.SessionManager
import androidx.swift.sword.env.ApiEnvironment
import androidx.swift.sword.env.ApiEnvironmentUrl
import androidx.swift.sword.provider.RetrofitFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidSwift.init(application)
        SessionManager.Builder(this.applicationContext, UserInfo::class.java).build()

        var user = SessionManager.getUser<UserInfo>()
        Log.d("Rae", "缓存用户$user")
        Log.d("rae", "自定义值：${SessionManager.getFloat("isFloat", 1f)}")
        Log.d("rae", "Object：${SessionManager.get("isObject", UserInfo::class.java)}")
        user = UserInfo("Rae", "123")

        SessionManager.setUser(user)
        Log.d("Rae", "设置用户$user")

        SessionManager.put("isBoolean", true)
        SessionManager.put("isString", "rae is a boy")
        SessionManager.put("isInt", 1314)
        SessionManager.put("isFloat", 3.14f)
        SessionManager.put("isLong", 3000L)
        SessionManager.put("isObject", UserInfo("newObject", "111"))

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

        RetrofitFactory.newBuilder()
            .debug(true)
            .tag("URL")
            .environment(ApiEnvironment.DEBUG)
            .addEnvironment(
                ApiEnvironmentUrl(
                    ApiEnvironment.DEBUG, "https://192.168.0.1/", mutableMapOf(
                        "map" to ""
                    )
                )
            )
            .build()
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