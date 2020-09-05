package com.elkassas.zar3tak

import android.content.Context
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_splash_screen.*


class SplashScreen : AppCompatActivity() {

    val inAnimation = AlphaAnimation(0f,1f)
    val outAnimation = AlphaAnimation(1f,0f)

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val usersRef = FirebaseDatabase.getInstance().getReference("Users")

    var currentUserType : String = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //Get Current User Type From Database
        getCurrentUserType()

        //Animation
        inAnimation.duration = 200
        outAnimation.duration = 200
        val myanim = AnimationUtils.loadAnimation(this@SplashScreen, R.anim.stb2)
        splash_screen_zar3tak_txt.animation = myanim

        Handler().postDelayed({
            //Check Internet Connection
            if (isConnected()){
                if (currentUser == null){
                    goToLogin()
                }else{
                    Handler().postDelayed({
                        try {
                           when(currentUserType){
                               "null" -> goToLogin()
                               "Customer" -> goToCustomerScreen()
                               "Seller" -> goToSellerScreen()
                           }
                        }catch (e : Exception){
                            Toast.makeText(applicationContext,e.message,Toast.LENGTH_LONG).show()
                        }
                    },2000)
                }

            }else{
               // Toast.makeText(this,"Check Your Connection",Toast.LENGTH_LONG).show()
                MaterialAlertDialogBuilder(
                    this,
                    R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
                )
                    .setTitle("Warning")
                    .setMessage("Check Your Internet Connection Then Come Back ..")
                    .setCancelable(false)
                    .setPositiveButton("Ok") { _, _ ->
                        finish()
                    }
                    .show()
            }

        },3000)
    }
    private fun goToLogin(){
        val intent = Intent(this@SplashScreen,Login::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }
    private fun goToCustomerScreen(){
        val intent = Intent(this@SplashScreen,CustomerScreen::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }
    private fun goToSellerScreen(){
        val intent = Intent(this@SplashScreen,SellerNavigation::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }
    private fun getCurrentUserType(){
        if (currentUser != null){
            val customerQuery : Query = usersRef.child("Customers").orderByChild("UserId").equalTo(currentUser.uid)
            val sellerQuery : Query = usersRef.child("Sellers").orderByChild("UserId").equalTo(currentUser.uid)

            customerQuery.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        if (snapshot.hasChildren()){
                            currentUserType = "Customer"
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext,error.message,Toast.LENGTH_LONG).show()
                }
            })
            sellerQuery.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        if (snapshot.hasChildren()){
                            currentUserType = "Seller"
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext,error.message,Toast.LENGTH_LONG).show()
                }
            })
        }
    }
    private fun isConnected(): Boolean {
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