package com.hiyama.anpikakuninproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hiyama.anpikakuninproject.activity.PasswordActivity


class MainActivity : AppCompatActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ }
        setUpPermissions()

        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        // 端末の戻るボタンが押された時の処理
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                /* do nothing */
            }
        })
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

    private fun setUpPermissions(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= 33) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val name = "my-notification-channel"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channelId = "${packageName}-$name"
                val channel = NotificationChannel(channelId, name, importance)
                channel.description = "my notification channel description"
                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

}