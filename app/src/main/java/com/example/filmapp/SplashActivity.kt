package com.example.filmapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.filmapp.Login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.filmapp.home.HomeActivity
class SplashActivity: AppCompatActivity() {
    val scope = CoroutineScope(Dispatchers.Main)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_splash)
        splashCoroutine()

    }
    fun splashCoroutine(){
        val intent = Intent(this, LoginActivity::class.java)
        scope.launch {
            delay(2000)
            startActivity(intent)
            finish()
        }
    }
}