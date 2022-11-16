package com.hiyama.anpikakuninproject.data

import android.content.Context
import androidx.appcompat.app.AlertDialog
import java.util.regex.Pattern

object DataChecker {
    /* ユーザが広島市立大学の学生であることを仮定し"7文字の数字"でチェック */
    fun isUserId(str:String, context: Context) : Boolean {
        val alpha = "[0-9]{7}"
        val p = Pattern.compile(alpha)
        val m = p.matcher(str)
        return if(m.matches()) {
            true
        } else {
            AlertDialog.Builder(context)
                .setTitle("●サインイン失敗")
                .setMessage("ユーザ名は数字7文字で入力してください")
                .setPositiveButton("OK") { _, _ -> }
                .show()
            false
        }
    }
}