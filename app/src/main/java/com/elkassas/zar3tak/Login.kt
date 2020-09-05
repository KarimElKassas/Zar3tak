package com.elkassas.zar3tak

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*


class Login : AppCompatActivity() {

    val inAnimation = AlphaAnimation(0f,2f)
    val outAnimation = AlphaAnimation(2f,0f)

    val mAuth = FirebaseAuth.getInstance()
    val users_ref = FirebaseDatabase.getInstance().getReference("Users")

    var user_token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Animation
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        login_remember_me_check_box.visibility = View.INVISIBLE

        //Animation
        inAnimation.duration = 200
        outAnimation.duration = 200

        login_sign_in_txt.translationX = 600f
        login_sign_in_txt.alpha = 0f
        login_sign_in_txt.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(500).start()

        login_email_edit_txt.translationX = 800f
        login_email_edit_txt.alpha = 0f
        login_email_edit_txt.animate().translationX(0f).alpha(1f).setDuration(1000).setStartDelay(500).start()

        login_password_edit_text.translationX = 1000f
        login_password_edit_text.alpha = 0f
        login_password_edit_text.animate().translationX(0f).alpha(1f).setDuration(1200).setStartDelay(500).start()

        login_sign_in_btn.translationX = 1200f
        login_sign_in_btn.alpha = 0f
        login_sign_in_btn.animate().translationX(0f).alpha(1f).setDuration(1400).setStartDelay(500).start()

        login_create_new_account_txt.translationX = 1400f
        login_create_new_account_txt.alpha = 0f
        login_create_new_account_txt.animate().translationX(0f).alpha(1f).setDuration(1600).setStartDelay(500).start()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                user_token = task.result!!.token

            }
        //SignIn
        login_sign_in_btn.setOnClickListener {
            dataValidation()
        }
        //Go To SignUp
        login_create_new_account_txt.setOnClickListener {
            goToSignUp()
        }

    }
    private fun dataValidation(){
        // User Email Validation
        if (TextUtils.isEmpty(login_email_edit_txt.text)){
            login_email_edit_txt.error = "Enter Your Email Please !"
            login_email_edit_txt.requestFocus()
            return
        }
        if (!login_email_edit_txt.text!!.contains("@")
            || !login_email_edit_txt.text!!.contains(".")
            || !login_email_edit_txt.text!!.contains("com")
            || !login_email_edit_txt.text!!.contains("gmail")){
            login_email_edit_txt.error = "Invalid Email Format !"
            login_email_edit_txt.requestFocus()
            return
        }
        // User Password Validation
        if (TextUtils.isEmpty(login_password_edit_text.text)){
            login_password_edit_text.error = "Enter Your Password Please !"
            login_password_edit_text.requestFocus()
            return
        }
        if (login_password_edit_text.text!!.length < 8){
            login_password_edit_text.error = "Password Must Be Greater Than 8 Characters !"
            login_password_edit_text.requestFocus()
            return
        }
        signIn()

    }
    private fun signIn(){
        val customerQuery: Query =
            users_ref.child("Customers").orderByChild("UserEmail").equalTo(login_email_edit_txt.text.toString())
        val sellerQuery : Query =
            users_ref.child("Sellers").orderByChild("UserEmail").equalTo(login_email_edit_txt.text.toString())

        customerQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    loginWithCustomer()

                } else {

                    sellerQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                loginWithSeller()
                            }else {
                                Toast.makeText(
                                    applicationContext,
                                    "Email Doesn't Exist !",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                            override fun onCancelled(databaseError: DatabaseError) {

                            }
                        })

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


    }
    private fun loginWithCustomer(){
        progressBarHolder_login.animation = inAnimation
        progressBarHolder_login.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        mAuth.signInWithEmailAndPassword(login_email_edit_txt.text.toString(),login_password_edit_text.text.toString())
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                        val user: FirebaseUser? = mAuth.currentUser

                        users_ref.child("Customers").child(user!!.uid).child("TokenId")
                            .setValue(user_token)
                            .addOnSuccessListener {

                                progressBarHolder_login.animation = outAnimation
                                progressBarHolder_login.visibility = View.GONE
                                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                                val intent = Intent(this, CustomerScreen::class.java)
                                intent.putExtra("CurrentUser", user.toString())
                                startActivity(intent)
                                overridePendingTransition(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left
                                )
                                this.finish()
                            }

                }else{
                    // If sign in fails, display a message to the user.
                    progressBarHolder_login.animation = outAnimation
                    progressBarHolder_login.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                    val errorCode =
                        (task.exception as FirebaseAuthException?)!!.errorCode
                    when (errorCode) {
                        "ERROR_WRONG_PASSWORD" -> {
                            login_password_edit_text.error = "Password is incorrect "
                            login_password_edit_text.requestFocus()
                        }
                    }
                    Toast.makeText(
                        this@Login, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }
    private fun loginWithSeller(){
        progressBarHolder_login.animation = inAnimation
        progressBarHolder_login.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        mAuth.signInWithEmailAndPassword(login_email_edit_txt.text.toString(),login_password_edit_text.text.toString())
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    val user: FirebaseUser? = mAuth.currentUser

                    users_ref.child("Sellers").child(user!!.uid).child("TokenId")
                        .setValue(user_token)
                        .addOnSuccessListener {

                            progressBarHolder_login.animation = outAnimation
                            progressBarHolder_login.visibility = View.GONE
                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                            val intent = Intent(this, SellerScreen::class.java)
                            intent.putExtra("CurrentUser", user.toString())
                            startActivity(intent)
                            overridePendingTransition(
                                R.anim.slide_in_right,
                                R.anim.slide_out_left
                            )
                            this.finish()
                        }

                }else{
                    // If sign in fails, display a message to the user.
                    progressBarHolder_login.animation = outAnimation
                    progressBarHolder_login.visibility = View.GONE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                    val errorCode =
                        (task.exception as FirebaseAuthException?)!!.errorCode
                    when (errorCode) {
                        "ERROR_WRONG_PASSWORD" -> {
                            login_password_edit_text.error = "Password is incorrect "
                            login_password_edit_text.requestFocus()
                        }
                    }
                    Toast.makeText(
                        this@Login, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    private fun goToSignUp(){
        val intent = Intent(this,SignUp::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }
}