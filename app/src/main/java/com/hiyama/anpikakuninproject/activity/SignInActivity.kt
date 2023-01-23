package com.hiyama.anpikakuninproject.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.DataChecker
import com.hiyama.anpikakuninproject.data.JsonParser
import com.hiyama.anpikakuninproject.data.SignInInfo
import com.hiyama.anpikakuninproject.data.SignInResultInfo
import com.hiyama.anpikakuninproject.utils.CommServer
import java.net.HttpURLConnection

class SignInActivity : AppCompatActivity() {

    private val commServer = CommServer()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val signInNumberEditText = findViewById<EditText>(R.id.signInNumber)
        val signInNameEditText = findViewById<EditText>(R.id.signInName)
        val signInEmailEditText = findViewById<EditText>(R.id.signInEmail)
        val passwordEditText = findViewById<EditText>(R.id.signInPassword)
        val againPasswordEditText = findViewById<EditText>(R.id.againPassWord)

        val cancelBtn = findViewById<Button>(R.id.signInCancelBtn)
        cancelBtn.setOnClickListener {
            finish()
        }

        val signInBtn = findViewById<Button>(R.id.signInBtn)
        signInBtn.setOnClickListener {
            val signInNumber = signInNumberEditText.text.toString()
            val signInName = signInNameEditText.text.toString()
            val signInEmail = signInEmailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val againPassword = againPasswordEditText.text.toString()
            if (checkCorrectEntered(signInEmail, signInNumber, password, againPassword)){
                SignInInfo.signInNumber = signInNumber
                SignInInfo.signInName = signInName
                SignInInfo.signInEmail = signInEmail
                SignInInfo.signInPassword = password
                commServer.setURL(CommServer.NEW_USER)
                signIn()
            }
        }

    }

    private fun signIn(): Boolean {
        val result = commServer.postInfo()
        while(commServer.responseCode == -1){/* wait for response */}
        if (commServer.responseCode == HttpURLConnection.HTTP_OK) {
            Log.i("Return Val From Server", "Value: $result")
            val signInResult = JsonParser.signInResultParse(result)
            return if (signInResult == null) {
                Toast.makeText(this, "新規登録の際にサーバから予期せぬメッセージを受信しました", Toast.LENGTH_LONG).show()
                false
            } else {
                SignInResultInfo.initialize(signInResult)
                if (SignInResultInfo.succeed){
                    AlertDialog.Builder(this)
                        .setTitle("●新規登録完了")
                        .setMessage("メールアドレスにメールを送信しました。\n${SignInResultInfo.message}")
                        .setPositiveButton("OK") { _, _ ->
                            finish()
                        }
                        .show()
                    true
                } else {
                    incorrectSignIn(this, SignInResultInfo.message)
                    false
                }
            }
        } else {
            incorrectSignIn(this, "予期せぬエラーが発生しました。")
            return false
        }
    }

    private fun checkCorrectEntered(signInEmail:String, signInNumber:String, password:String, againPassword:String) : Boolean {
        /* 文字列が入力されていない */
        if(signInEmail.isEmpty() || signInNumber.isEmpty() || password.isEmpty() || againPassword.isEmpty()) {
            incorrectSignIn(this, "情報をすべて入力してください")
            return false
        }

        /* ユーザ名の確認 */
        if (!DataChecker.isUserId(signInNumber, this)) return false

        if (password != againPassword) {
            incorrectSignIn(this, "確認パスワードが一致しません")
            return false
        }
        return true
    }

    private fun incorrectSignIn(context: Context, message: String) {
        AlertDialog.Builder(context)
            .setTitle("●新規登録失敗")
            .setMessage(message)
            .setPositiveButton("OK") { _, _ -> }
            .show()
    }

    override fun onBackPressed() { // 端末の戻るボタンが押された時
        /* do nothing */
    }
}