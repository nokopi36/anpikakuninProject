package com.hiyama.anpikakuninproject.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hiyama.anpikakuninproject.CommServer
import com.hiyama.anpikakuninproject.R
import com.hiyama.anpikakuninproject.data.ChangePasswordResultInfo
import com.hiyama.anpikakuninproject.data.JsonParser
import com.hiyama.anpikakuninproject.data.PassWordInfo
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection
import java.security.MessageDigest

class PasswordActivity : AppCompatActivity()  {

    private val commServer = CommServer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        val oldPasswordEditText = findViewById<EditText>(R.id.oldPassword)
        val newPasswordEditText = findViewById<EditText>(R.id.newPassWord)

        val changePasswordBtn = findViewById<Button>(R.id.changePasswordBtn)
        changePasswordBtn.setOnClickListener { // パスワード変更のためのボタン
            val oldPassWord = hashSHA256String(oldPasswordEditText.text.toString())
            val newPassWord = hashSHA256String(newPasswordEditText.text.toString())
            if (checkCorrectEntered(oldPasswordEditText.text.toString(), newPasswordEditText.text.toString())){
                PassWordInfo.old_password = oldPassWord
                PassWordInfo.new_password = newPassWord
                commServer.setURL(CommServer.CHANGE_PASSWORD)
                if (!changePassword()){
                    oldPasswordEditText.text.clear()
                    newPasswordEditText.text.clear()
                }
            }
        }

    }

    private fun changePassword(): Boolean {
        val result = postInfo()
        while(commServer.responseCode == -1){/* wait for response */}
        if (commServer.responseCode == HttpURLConnection.HTTP_OK){
            Log.i("Return Value From Server", "Value: $result")
            val changePasswordResult = JsonParser.changePasswordParse(result)
            return if (changePasswordResult == null){
                Toast.makeText(this, "ログインの際にサーバから予期せぬメッセージを受信しました", Toast.LENGTH_LONG).show()
                false
            } else {
                ChangePasswordResultInfo.initialize(changePasswordResult)
                if (ChangePasswordResultInfo.success){
                    Toast.makeText(this, "パスワード変更完了しました", Toast.LENGTH_SHORT).show()
                    finish()
                    true
                } else {
                    incorrectChangePassword(this)
                    false
                }
            }
        } else {
            incorrectChangePassword(this)
            return false
        }
    }

    @UiThread
    private fun postInfo(): String{ //posttest
        var result = ""
        runBlocking { // postして結果が返ってくるまで待機
            result = commServer.postInfoBackGroundRunner("UTF-8")
            Log.i("POST",result)
        }
        return result
    }

    private fun incorrectChangePassword(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("●パスワード変更失敗")
            .setMessage("現在のパスワードが間違っています")
            .setPositiveButton("OK") { _, _ -> }
            .show()
    }

    private fun checkCorrectEntered(oldPassword:String, newPassWord: String) : Boolean {
        Log.e(">>>","OLD_PASSWORD:$oldPassword, NEW_PASSWORD:$newPassWord")

        /* 文字列が入力されていない */
        if(oldPassword.isEmpty() || newPassWord.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("●パスワード変更失敗")
                .setMessage("パスワードが入力されていません")
                .setPositiveButton("OK") { _, _ -> }
                .show()
            return false
        }

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

}