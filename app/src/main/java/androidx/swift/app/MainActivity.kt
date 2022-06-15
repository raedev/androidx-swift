package androidx.swift.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.swift.session.SessionManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var user: UserInfo? = UserInfo("Rae", "123")
        SessionManager.Builder(this.applicationContext, UserInfo::class.java)
            .jsonProvider()
            .enableEncrypt(true)
            .build()

        Log.d("Rae", "设置用户$user")
        SessionManager.default.setUser(user)
        user = SessionManager.default.getUser<UserInfo>()

        Log.d("Rae", "缓存用户$user")

    }
}

data class UserInfo(
    var name: String,
    var password: String
)