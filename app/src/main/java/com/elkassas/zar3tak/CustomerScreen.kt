package com.elkassas.zar3tak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class CustomerScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_screen)
        Toast.makeText(this,"Customer Screen", Toast.LENGTH_SHORT).show()
    }
}