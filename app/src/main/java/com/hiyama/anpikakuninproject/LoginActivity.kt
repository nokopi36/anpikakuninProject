package com.hiyama.anpikakuninproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.coroutines.launch
import com.google.firebase.messaging.FirebaseMessaging
import com.hiyama.anpikakuninproject.data.User
import com.hiyama.anpikakuninproject.data.UserInfo

class LoginActivity : AppCompatActivity() {

    val commServer = CommServer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val userName = findViewById<EditText>(R.id.userName)
        val password = findViewById<EditText>(R.id.passWord)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            UserInfo.userName = userName.text.toString()
            UserInfo.password = password.text.toString()
            val postData = jacksonObjectMapper().writeValueAsString(User.getUserInfo())
            Log.i("postData",postData )
            startActivity(intent)
            overridePendingTransition(0,0)
        }

        val testBtn = findViewById<Button>(R.id.testBtn) //ServerからGETできるかテストするためのボタン
        testBtn.setOnClickListener {
            commServer.setURL(CommServer.TEST)
            getInfo()
            val postData = jacksonObjectMapper().writeValueAsString(PostTest.getPostData())
            Log.i("postData",postData )
        }

        val postBtn = findViewById<Button>(R.id.postBtn) //ServerからPOSTできるかテストするためのボタン
        postBtn.setOnClickListener {
            commServer.setURL(CommServer.POSTTEST)
            postInfo()
        }

        // インスタンスIDの自動生成を有効化する場合、true
        // AndroidManifestにて自動生成を禁止にしていない場合、不要
//        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        // Current Notificationトークンの取得
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    // token取得失敗
                    Log.d("getInstanceId failed ${task.exception}", "a")
                    return@OnCompleteListener
                }

                // new Instance ID token
                // ここで取得したtokenをテストする際のインスタンスIDとして設定する
                val token = task.result

                val msg = "InstanceID Token: $token"
                Log.d("msg",msg)
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })

    }

    @UiThread
    private fun getInfo(){
        val testTxt = findViewById<TextView>(R.id.testText)
        lifecycleScope.launch {
            val result = commServer.getInfoBackGroundRunner("UTF-8")
            Log.i("GET",result)
            testTxt.text = result
        }
    }

    @UiThread
    private fun postInfo(){ //posttest
        val testTxt = findViewById<TextView>(R.id.postText)
        lifecycleScope.launch {
            val result = commServer.postInfoBackGroundRunner("UTF-8")
            Log.i("POST",result)
            testTxt.text = result
        }
    }

    fun saveAccount() {
        val editor :SharedPreferences.Editor = getSharedPreferences("ID_STORAGE", MODE_PRIVATE).edit()
        val primaryUserNumber = findViewById<EditText>(R.id.userName).text.toString()
        val primaryPassword = findViewById<EditText>(R.id.passWord).text.toString()
        editor.putString("PRIMARY_USERNUMBER", primaryUserNumber)
        editor.putString("PRIMARY_PASSWORD", primaryPassword)
        editor.commit()
    }

}