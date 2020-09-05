package com.elkassas.zar3tak

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            //Check Internet Connection
            if (isConnected()){
                val intent = Intent(this@SplashScreen,SignUp::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }else{
               // Toast.makeText(this,"Check Your Connection",Toast.LENGTH_LONG).show()
                MaterialAlertDialogBuilder(
                    this,
                    R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
                )
                    .setTitle("Warning")
                    .setMessage("Check Your Internet Connection Then Come Back ..")
                    .setCancelable(false)
                    .setPositiveButton("Ok",  DialogInterface.OnClickListener { _, _ ->
                        finish()
                    })
                    .show()
            }

        },3000)
    }
    fun isConnected(): Boolean {
        var connected = false
        try {
            val cm =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
            return connected
        } catch (e: Exception) {
            Log.e("Connectivity Exception", e.message.toString())
        }
        return connected
    }
}