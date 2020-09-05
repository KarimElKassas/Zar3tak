package com.elkassas.zar3tak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_customer_screen.*

class CustomerScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_screen)
        Toast.makeText(this,"Customer Screen", Toast.LENGTH_SHORT).show()
        customer_button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
        }
    }
}