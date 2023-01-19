package com.hiyama.anpikakuninproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hiyama.anpikakuninproject.activity.PasswordActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.toolbar_layout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.changePassword -> {
                val intent = Intent(this, PasswordActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.report -> {
                AlertDialog.Builder(this)
                    .setTitle("報告")
                    .setMessage("不具合報告やその他報告はTwitterのDMもしくはFormsへ")
                    .setPositiveButton("Twitterへ") { _, _ ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/AppHcu?t=EmIIQJpOsAWvm85PPRPiAg&s=09"))
                        startActivity(intent)
                    }
                    .setNegativeButton("Google Formsへ") { _, _ ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/FsxZvaGRistbbhuK8"))
                        startActivity(intent)
                    }
                    .setNeutralButton("キャンセル") { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() { // 端末の戻るボタンが押された時
        /* do nothing */
    }

}