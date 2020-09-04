package com.elkassas.zar3tak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    val inAnimation = AlphaAnimation(0f,1f)
    val outAnimation = AlphaAnimation(1f,0f)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //Animation
        inAnimation.duration = 200
        outAnimation.duration = 200
        val myanim = AnimationUtils.loadAnimation(this@SplashScreen, R.anim.stb2)
        splash_screen_zar3tak_txt.animation = myanim

        Handler().postDelayed({
            val intent = Intent(this@SplashScreen,SignUp::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        },3000)
    }
}