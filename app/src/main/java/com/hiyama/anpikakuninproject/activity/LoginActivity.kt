package com.hiyama.anpikakuninproject.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.coroutines.launch
import com.google.firebase.messaging.FirebaseMessaging
import com.hiyama.anpikakuninproject.CommServer
import com.hiyama.anpikakuninproject.MainActivity
import com.hiyama.anpikakuninproject.data.PostTest
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.JsonParser
import com.hiyama.anpikakuninproject.data.PostInfo
import com.hiyama.anpikakuninproject.data.User
import com.hiyama.anpikakuninproject.data.UserInfo
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection

class LoginActivity : AppCompatActivity() {

    private val commServer = CommServer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val userName = findViewById<EditText>(R.id.userName)
        val password = findViewById<EditText>(R.id.passWord)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {// ログインするためのボタン
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

        val postBtn = findViewById<Button>(R.id.postBtn) //ServerにPOSTできるかテストするためのボタン
        postBtn.setOnClickListener {
            commServer.setURL(CommServer.POSTTEST)
            postTest()
        }

        /*------------------ここからpush通知に関すること------------------*/
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
//                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })
        /*------------------ここまで------------------*/

    }

    private fun login(): Boolean{
        val result = postInfo()
        while(commServer.responseCode == -1){/* wait for response */}
        if (commServer.responseCode == HttpURLConnection.HTTP_OK){
            Log.i("Return Value From Server", "Value: $result")
//            val result = postInfo()
            if (result != "null"){
                val user = JsonParser.userParse(result)
                return if (user == null){
                    Toast.makeText(this, "ログインの際にサーバから予期せぬメッセージを受信しました", Toast.LENGTH_LONG).show()
                    false
                } else {
                    UserInfo.initialize(user)
                    Toast.makeText(this, "${UserInfo.userName}さん ようこそ！", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
            } else {
                incorrectSignIn(this)
                return false
            }
        } else {
            incorrectSignIn(this)
            return false
        }
    }

    private fun postTest(): Boolean{
        val postSuccess = findViewById<TextView>(R.id.postSuccess)
        val postMessage = findViewById<TextView>(R.id.postMessage)
        val postToken = findViewById<TextView>(R.id.postToken)

        val result = postInfo()
        Log.i("postTestResult", result)
        while(commServer.responseCode == -1){/* wait for response */}
        Log.i("resposeCode", commServer.responseCode.toString())
        if (commServer.responseCode == HttpURLConnection.HTTP_OK){
            Log.i("Return Value From Server", "Value: $result")
//            val result = postInfo()
            if (result != "null"){
                val postResultTest = JsonParser.postResultTestParse(result)
                Log.i("postResuleTest", postResultTest.toString())
                return if (postResultTest == null){
                    Toast.makeText(this, "ログインの際にサーバから予期せぬメッセージを受信しました", Toast.LENGTH_LONG).show()
                    false
                } else {
                    PostInfo.initialize(postResultTest)
                    postSuccess.text = PostInfo.success.toString()
                    Log.i("success", PostInfo.success.toString())
                    postMessage.text = PostInfo.message
                    Log.i("message", PostInfo.message)
                    postToken.text = PostInfo.token
                    Log.i("token", PostInfo.token)

                    true
                }
            } else {
                incorrectSignIn(this)
                return false
            }
        } else {
            incorrectSignIn(this)
            return false
        }
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
    private fun postInfo(): String{ //posttest
        val testTxt = findViewById<TextView>(R.id.postText)
        var result = ""
        runBlocking { // postして結果が返ってくるまで待機
            result = commServer.postInfoBackGroundRunner("UTF-8")
            Log.i("POST",result)
            testTxt.text = result
        }
        return result
    }

    private fun incorrectSignIn(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("●サインイン失敗")
            .setMessage("ユーザ名もしくはパスワードが間違っています")
            .setPositiveButton("OK") { _, _ -> }
            .show()
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