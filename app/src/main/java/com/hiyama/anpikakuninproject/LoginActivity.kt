package com.hiyama.anpikakuninproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    val commServer = CommServer()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)
        }

        val testBtn = findViewById<Button>(R.id.testBtn)
        testBtn.setOnClickListener {
            commServer.setURL(CommServer.TEST)
            receiveInfo()
        }

    }

    @UiThread
    private fun receiveInfo(){
        val testTxt = findViewById<TextView>(R.id.testText)
        lifecycleScope.launch {
            val result = commServer.getInfoBackGroundRunner("UTF-8")
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