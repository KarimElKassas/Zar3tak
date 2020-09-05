package com.elkassas.zar3tak

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.IOException

class SignUp : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    val users_ref = FirebaseDatabase.getInstance().getReference("Users")
    val storage_ref = FirebaseStorage.getInstance().reference

    val User_Data = HashMap<String,String>()
    var gabelsora : Boolean = false

    private val GALLERY_INTENT = 1
    private var imgUri: Uri? = null
    var user_token : String? = ""

    val inAnimation = AlphaAnimation(0f,2f)
    val outAnimation = AlphaAnimation(2f,0f)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //Animation
        inAnimation.duration = 200
        outAnimation.duration = 200

        sign_up_register_text.translationX = 400f
        sign_up_register_text.alpha = 0f
        sign_up_register_text.animate().translationX(0f).alpha(1f).setDuration(600).setStartDelay(500).start()

        sign_up_create_an_account_text.translationX = 600f
        sign_up_create_an_account_text.alpha = 0f
        sign_up_create_an_account_text.animate().translationX(0f).alpha(1f).setDuration(800).setStartDelay(500).start()

        sign_up_user_img.translationX = 800f
        sign_up_user_img.alpha = 0f
        sign_up_user_img.animate().translationX(0f).alpha(1f).setDuration(1000).setStartDelay(500).start()

        sign_up_user_name_edit_txt.translationX = 1000f
        sign_up_user_name_edit_txt.alpha = 0f
        sign_up_user_name_edit_txt.animate().translationX(0f).alpha(1f).setDuration(1200).setStartDelay(500).start()

        sign_up_email_edit_txt.translationX = 1200f
        sign_up_email_edit_txt.alpha = 0f
        sign_up_email_edit_txt.animate().translationX(0f).alpha(1f).setDuration(1400).setStartDelay(500).start()

        sign_up_password_edit_txt.translationX = 1400f
        sign_up_password_edit_txt.alpha = 0f
        sign_up_password_edit_txt.animate().translationX(0f).alpha(1f).setDuration(1600).setStartDelay(500).start()

        sign_up_phone_edit_txt.translationX = 1600f
        sign_up_phone_edit_txt.alpha = 0f
        sign_up_phone_edit_txt.animate().translationX(0f).alpha(1f).setDuration(1800).setStartDelay(500).start()

        sign_up_national_id_edit_txt.translationX = 1800f
        sign_up_national_id_edit_txt.alpha = 0f
        sign_up_national_id_edit_txt.animate().translationX(0f).alpha(1f).setDuration(2000).setStartDelay(500).start()

        sign_up_radio_group.translationX = 2000f
        sign_up_radio_group.alpha = 0f
        sign_up_radio_group.animate().translationX(0f).alpha(1f).setDuration(2200).setStartDelay(500).start()

        sign_up_sign_up_btn.translationX = 2200f
        sign_up_sign_up_btn.alpha = 0f
        sign_up_sign_up_btn.animate().translationX(0f).alpha(1f).setDuration(2400).setStartDelay(500).start()

        sign_up_sign_in_txt.translationX = 2400f
        sign_up_sign_in_txt.alpha = 0f
        sign_up_sign_in_txt.animate().translationX(0f).alpha(1f).setDuration(2600).setStartDelay(500).start()

        //To Get User Token ID
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                user_token = task.result!!.token

            }
        // To Get User Img From Gallery
        sign_up_user_img.setOnClickListener{
            uploadProfile()
        }
        sign_up_sign_up_btn.setOnClickListener{
            dataValidation()
        }
        // go To Login
        sign_up_sign_in_txt.setOnClickListener {
            goToLogin()
        }

    }

    private fun dataValidation(){
        // User Img Validation
        if (!gabelsora){
            Toast.makeText(this,"Select Your Profile Pic Please !",Toast.LENGTH_LONG).show()
            return
        }
        // User Name Validation
        if (TextUtils.isEmpty(sign_up_user_name_edit_txt.text)){
            sign_up_user_name_edit_txt.error = "Enter Your Name Please !"
            sign_up_user_name_edit_txt.requestFocus()
            return
        }
        // User Email Validation
        if (TextUtils.isEmpty(sign_up_email_edit_txt.text)){
            sign_up_email_edit_txt.error = "Enter Your Email Please !"
            sign_up_email_edit_txt.requestFocus()
            return
        }
        if (!sign_up_email_edit_txt.text!!.contains("@")
                || !sign_up_email_edit_txt.text!!.contains(".")
                || !sign_up_email_edit_txt.text!!.contains("com")
                || !sign_up_email_edit_txt.text!!.contains("gmail")){
                 sign_up_email_edit_txt.error = "Invalid Email Format !"
                sign_up_email_edit_txt.requestFocus()
                return
        }
        // User Password Validation
        if (TextUtils.isEmpty(sign_up_password_edit_txt.text)){
            sign_up_password_edit_txt.error = "Enter Your Password Please !"
            sign_up_password_edit_txt.requestFocus()
            return
        }
        if (sign_up_password_edit_txt.text!!.length < 8){
            sign_up_password_edit_txt.error = "Password Must Be Greater Than 8 Characters !"
            sign_up_password_edit_txt.requestFocus()
            return
        }
        // User Phone Validation
        if (TextUtils.isEmpty(sign_up_phone_edit_txt.text)){
            sign_up_phone_edit_txt.error = "Enter Your Phone Number Please !"
            sign_up_phone_edit_txt.requestFocus()
            return
        }
        if (!sign_up_phone_edit_txt.text!!.startsWith("01")){
            sign_up_phone_edit_txt.error = "Phone Number Must Starts With 01 !"
            sign_up_phone_edit_txt.requestFocus()
            return
        }
        if (sign_up_phone_edit_txt.text!!.length != 11){
            sign_up_phone_edit_txt.error = "Phone Number Must Be 11 Number Only !"
            sign_up_phone_edit_txt.requestFocus()
            return
        }
        // User National ID Validation
        if (TextUtils.isEmpty(sign_up_national_id_edit_txt.text)){
            sign_up_national_id_edit_txt.error = "Enter Your National ID Please !"
            sign_up_national_id_edit_txt.requestFocus()
            return
        }
        if (sign_up_national_id_edit_txt.text!!.length != 14){
            sign_up_national_id_edit_txt.error = "National ID Must Be 14 Number Only !"
            sign_up_national_id_edit_txt.requestFocus()
            return
        }
        //User Identifier Validation
        if (!sign_up_customer_radio.isChecked && !sign_up_seller_radio.isChecked){
            Toast.makeText(this,"Select Your Identifier Please !",Toast.LENGTH_LONG).show()
            return
        }

        createNewUser()
    }
    private fun createNewUser() {
        // Progress Bar
        sign_up_progressBarHolder.animation = inAnimation
        sign_up_progressBarHolder.visibility = View.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

        mAuth.createUserWithEmailAndPassword(
            sign_up_email_edit_txt.text.toString(),
            sign_up_password_edit_txt.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (!task.isSuccessful) {
                    if (task.exception is FirebaseAuthUserCollisionException) {

                        sign_up_progressBarHolder.animation = outAnimation
                        sign_up_progressBarHolder.visibility = View.GONE
                        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                        Toast.makeText(
                            applicationContext,
                            "This Email already exists",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@addOnCompleteListener
                    }
                } else {
                    val current_user = mAuth.currentUser
                    imgUri?.let {
                        if (sign_up_customer_radio.isChecked) {
                            storage_ref.child("ProfileImages/Customers/" + current_user!!.uid)
                                .putFile(it)
                                .addOnSuccessListener {
                                    storage_ref.child("ProfileImages/Customers/" + current_user.uid).downloadUrl
                                        .addOnSuccessListener { uri ->
                                            User_Data["UserName"] =
                                                sign_up_user_name_edit_txt.text.toString()
                                            User_Data["UserEmail"] =
                                                sign_up_email_edit_txt.text.toString()
                                            User_Data["UserPassword"] =
                                                sign_up_password_edit_txt.text.toString()
                                            User_Data["UserPhone"] =
                                                sign_up_phone_edit_txt.text.toString()
                                            User_Data["UserNationalID"] =
                                                sign_up_national_id_edit_txt.text.toString()
                                            User_Data["TokenId"] = user_token.toString()
                                            User_Data["UserImg"] = uri.toString()
                                            User_Data["UserId"] = current_user.uid

                                            users_ref.child("Customers").child(current_user.uid)
                                                .setValue(User_Data)
                                                .addOnSuccessListener {

                                                    sign_up_progressBarHolder.animation =
                                                        outAnimation
                                                    sign_up_progressBarHolder.visibility = View.GONE
                                                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                                                    Toast.makeText(
                                                        applicationContext,
                                                        "Sign Up Successfully",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                    clearData()
                                                    goToLogin()

                                                }.addOnFailureListener {

                                                    sign_up_progressBarHolder.animation =
                                                        outAnimation
                                                    sign_up_progressBarHolder.visibility = View.GONE
                                                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                                                    Toast.makeText(
                                                        applicationContext,
                                                        "Sign Up Failed",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                }
                                        }

                                }

                        } else {
                            storage_ref.child("ProfileImages/Sellers/" + current_user!!.uid)
                                .putFile(it)
                                .addOnSuccessListener {
                                    storage_ref.child("ProfileImages/Sellers/" + current_user.uid).downloadUrl
                                        .addOnSuccessListener { uri ->
                                            User_Data["UserName"] =
                                                sign_up_user_name_edit_txt.text.toString()
                                            User_Data["UserEmail"] =
                                                sign_up_email_edit_txt.text.toString()
                                            User_Data["UserPassword"] =
                                                sign_up_password_edit_txt.text.toString()
                                            User_Data["UserPhone"] =
                                                sign_up_phone_edit_txt.text.toString()
                                            User_Data["UserNationalID"] =
                                                sign_up_national_id_edit_txt.text.toString()
                                            User_Data["TokenId"] = user_token.toString()
                                            User_Data["UserImg"] = uri.toString()
                                            User_Data["UserId"] = current_user.uid

                                            users_ref.child("Sellers").child(current_user.uid)
                                                .setValue(User_Data)
                                                .addOnSuccessListener {

                                                    sign_up_progressBarHolder.animation =
                                                        outAnimation
                                                    sign_up_progressBarHolder.visibility = View.GONE
                                                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                                                    Toast.makeText(
                                                        applicationContext,
                                                        "Sign Up Successfully",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                    clearData()
                                                    goToLogin()

                                                }.addOnFailureListener {

                                                    sign_up_progressBarHolder.animation =
                                                        outAnimation
                                                    sign_up_progressBarHolder.visibility = View.GONE
                                                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                                                    Toast.makeText(
                                                        applicationContext,
                                                        "Sign Up Failed",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                }
                                        }

                                }

                        }
                    }
                }
            }
    }

    private fun clearData(){
        sign_up_user_img.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_add_user,null))
        sign_up_user_name_edit_txt.text!!.clear()
        sign_up_email_edit_txt.text!!.clear()
        sign_up_password_edit_txt.text!!.clear()
        sign_up_phone_edit_txt.text!!.clear()
        sign_up_national_id_edit_txt.text!!.clear()
    }
    private fun goToLogin(){
        val intent = Intent(this,Login::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }
    private fun uploadProfile() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "select"),GALLERY_INTENT)
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == GALLERY_INTENT && resultCode == Activity.RESULT_OK) {
                imgUri = data!!.data
                //Toast.makeText(this, imgUri.toString(), Toast.LENGTH_LONG).show()
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imgUri)
                    sign_up_user_img.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    //Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }
                gabelsora = true
            }
        }catch (ex : Exception){
            Toast.makeText(applicationContext,ex.message,Toast.LENGTH_LONG).show()
        }

    }

}