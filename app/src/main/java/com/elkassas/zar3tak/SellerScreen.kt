package com.elkassas.zar3tak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class SellerScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_screen)
        Toast.makeText(this,"Seller Screen",Toast.LENGTH_SHORT).show()
    }
}