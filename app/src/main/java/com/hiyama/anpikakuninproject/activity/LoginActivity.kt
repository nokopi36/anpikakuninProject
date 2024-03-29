package com.hiyama.anpikakuninproject.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.hiyama.anpikakuninproject.utils.CommServer
import com.hiyama.anpikakuninproject.MainActivity
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.DataChecker
import com.hiyama.anpikakuninproject.data.JsonParser
import com.hiyama.anpikakuninproject.data.LoginInfo
import com.hiyama.anpikakuninproject.data.SignInInfo
import com.hiyama.anpikakuninproject.data.UserInfo
import java.net.HttpURLConnection
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private val commServer = CommServer()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val userNameEditText = findViewById<EditText>(R.id.userName)
        val passwordEditText = findViewById<EditText>(R.id.passWord)
//        val testTxt = findViewById<TextView>(R.id.testText)

        val versionNameText = findViewById<TextView>(R.id.versionName)

        val version: String
        try {
            val packageName = packageName
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            version = packageInfo.versionName
            "ver.$version".also { versionNameText.text = it }
        } catch (e: Exception){
            e.printStackTrace()
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
                UserInfo.fcmToken = token
                SignInInfo.fcm_token = token

                val msg = "InstanceID Token: $token"
                Log.d("msg",msg)
//                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })
        /*------------------ここまで------------------*/

        /*以下2つはtestServerに接続するときはコメントアウトする*/
        autoLogin()

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener { // ログインするためのボタン
            val userName = userNameEditText.text.toString()
            val passWord = passwordEditText.text.toString()
//            if (checkCorrectEntered(userName, passWord) && testServerLogin()){ // testServerに接続しないときはコメントアウトして下の行を有効化する
            if (checkCorrectEntered(userName, passWord)){
                UserInfo.userName = userName
//                UserInfo.password = hashSHA256String(passWord)
                UserInfo.password = passWord
                sharedPreferences.edit().putString("userName", UserInfo.userName).apply()
                sharedPreferences.edit().putString("password", passWord).apply()
                sharedPreferences.edit().putString("fcmToken", UserInfo.fcmToken).apply()
                commServer.setURL(CommServer.LOGIN)
                if (!login()){
                    passwordEditText.text.clear()
                }
            }
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
            overridePendingTransition(0,0)
        }

        val newUserBtn = findViewById<Button>(R.id.signInBtn)
        newUserBtn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

//        val testBtn = findViewById<Button>(R.id.testBtn) //ServerからGETできるかテストするためのボタン
//        testBtn.setOnClickListener {
//            commServer.setURL(CommServer.TEST)
//            val result = getInfo()
//            testTxt.text = result
//            val postData = jacksonObjectMapper().writeValueAsString(PostTest.getPostData())
//            Log.i("postData",postData )
//        }
//
//        val postBtn = findViewById<Button>(R.id.postBtn) //ServerにPOSTできるかテストするためのボタン
//        postBtn.setOnClickListener {
//            commServer.setURL(CommServer.POSTTEST)
//            postTest()
//        }
    }

    private fun login(): Boolean {
        val result = commServer.postInfo()
        while(commServer.responseCode == -1){/* wait for response */}
        when (commServer.responseCode) {
            HttpURLConnection.HTTP_OK -> {
                Log.i("Return Val From Server", "Value: $result")
                val loginResult = JsonParser.loginResultParse(result)
                return if (loginResult == null) {
                    Toast.makeText(this, "ログインの際にサーバから予期せぬメッセージを受信しました", Toast.LENGTH_LONG).show()
                    false
                } else {
                    LoginInfo.initialize(loginResult)
                    if (LoginInfo.succeed) {
                        Toast.makeText(this, "${UserInfo.userName}さん ようこそ！", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        true
                    } else {
                        incorrectLogin(this, LoginInfo.message)
                        false
                    }
                }
            }
            0 -> {
                connectionTimeout(this)
                return false
            }
            else -> {
                incorrectLogin(this, "予期せぬエラーが発生しました。")
                return false
            }
        }
    }

    private fun incorrectLogin(context: Context, message: String) {
        AlertDialog.Builder(context)
            .setTitle("●サインイン失敗")
            .setMessage(message)
            .setPositiveButton("OK") { _, _ -> }
            .show()
    }

    private fun connectionTimeout(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("●接続失敗")
            .setMessage("サーバーに接続できません")
            .setPositiveButton("アプリを終了する") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun checkCorrectEntered(userName:String, password:String) : Boolean {
        Log.e(">>>","USERNAME:$userName, PASSWORD:$password")

        /* 文字列が入力されていない */
        if(userName.isEmpty() || password.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("●ログイン失敗")
                .setMessage("学籍番号もしくはパスワードが入力されていません")
                .setPositiveButton("OK") { _, _ -> }
                .show()
            return false
        }

        /* ユーザ名の確認 */
        if(!DataChecker.isUserId(userName, this)) return false

        return true
    }

    private fun hashSHA256String(target: String): String {
        val hashBytes = MessageDigest.getInstance("SHA-256").digest(target.toByteArray())
        val hexChars = "0123456789abcdef"
        val result = StringBuilder(hashBytes.size * 2)
        hashBytes.forEach {
            val i = it.toInt()
            result.append(hexChars[i shr 4 and 0x0f])
            result.append(hexChars[i and 0x0f])
        }
        return result.toString()
    }

    private fun autoLogin(){
        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        val savedUserName = sharedPreferences.getString("userName", "NO_USERNAME")
        val savedPassWord = sharedPreferences.getString("password", "NO_PASSWORD")
        val savedFcmToken = sharedPreferences.getString("fcmToken", "NO_TOKEN")
        Log.i(">>>", "sharedUser:$savedUserName, sharedPass:$savedPassWord")
        if (savedUserName == "NO_USERNAME" || savedPassWord == "NO_PASSWORD"){
            /* do nothing */
        } else {
            UserInfo.userName = savedUserName!!
//            UserInfo.password = hashSHA256String(savedPassWord!!)
            UserInfo.password = savedPassWord!!
            UserInfo.fcmToken = savedFcmToken!!
            commServer.setURL(CommServer.LOGIN)
            login()
        }
    }


//    private fun postTest(): Boolean{
//        val postSuccess = findViewById<TextView>(R.id.postSuccess)
//        val postMessage = findViewById<TextView>(R.id.postMessage)
//        val postToken = findViewById<TextView>(R.id.postToken)
//
//        val result = postInfo()
//        Log.i("postTestResult", result)
//        while(commServer.responseCode == -1){/* wait for response */}
//        Log.i("responseCode", commServer.responseCode.toString())
//        if (commServer.responseCode == HttpURLConnection.HTTP_OK){
//            Log.i("Return Val From Server", "Value: $result")
//            if (result != "null"){
//                val postResultTest = JsonParser.loginResultParse(result)
//                Log.i("postResultTest", postResultTest.toString())
//                return if (postResultTest == null){
//                    Toast.makeText(this, "ログインの際にサーバから予期せぬメッセージを受信しました", Toast.LENGTH_LONG).show()
//                    false
//                } else {
//                    LoginInfo.initialize(postResultTest)
//                    postSuccess.text = LoginInfo.success.toString()
//                    Log.i("success", LoginInfo.success.toString())
//                    postMessage.text = LoginInfo.message
//                    Log.i("message", LoginInfo.message)
//                    postToken.text = LoginInfo.token
//                    Log.i("token", LoginInfo.token)
//                    true
//                }
//            } else {
//                incorrectLogin(this)
//                return false
//            }
//        } else {
//            incorrectLogin(this)
//            return false
//        }
//    }

//    @UiThread
//    private fun getInfo(): String{
//        var result: String
//        runBlocking {
//            result = commServer.getInfoBackGroundRunner("UTF-8")
//            Log.i("GET",result)
//        }
//        return result
//    }

//    @UiThread
//    private fun postInfo(): String{ //postTest
//        val postTxt = findViewById<TextView>(R.id.postText)
//        var result: String
//        runBlocking { // postして結果が返ってくるまで待機
//            result = commServer.postInfoBackGroundRunner("UTF-8")
//            Log.i("POST",result)
//            postTxt.text = result
//        }
//        return result
//    }

//    @UiThread
//    private fun loginInfo(): String{ //postTest
//        var result: String
//        runBlocking { // postして結果が返ってくるまで待機
//            result = commServer.postInfoBackGroundRunner("UTF-8")
//            Log.i("POST",result)
//        }
//        return result
//    }
    // testServerに接続するときに使う関数
//    private fun testServerLogin(): Boolean{
//        val testServerIP = findViewById<EditText>(R.id.testServerIP)
//        val testServerPort = findViewById<EditText>(R.id.testServerPort)
//        return if (testServerIP.text.toString().isEmpty() && testServerPort.text.toString().isEmpty()){
//            CommServer.ipAddress = "160.248.2.236"
//            CommServer.port = "3000"
//            true
//        } else if (testServerIP.text.toString().isEmpty() || testServerPort.text.toString().isEmpty()){
//            AlertDialog.Builder(this)
//                .setMessage("IPもしくはPortが入力されていません")
//                .setPositiveButton("OK") { _, _ -> }
//                .show()
//            false
//        } else {
//            CommServer.ipAddress = testServerIP.text.toString()
//            CommServer.port = testServerPort.text.toString()
//            true
//        }
//    }

}