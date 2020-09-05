package com.elkassas.zar3tak

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_seller_navigation.*

class SellerNavigation : AppCompatActivity() {

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Home -> {
                  //  Toast.makeText(applicationContext,"Home Clicked",Toast.LENGTH_SHORT).show()
                    val seller_select_category_fragment = SellerSelectCategoryFragment()
                    val fragmentTransaction1 =
                        supportFragmentManager.beginTransaction()
                    fragmentTransaction1.replace(R.id.seller_frame_content, seller_select_category_fragment)
                    fragmentTransaction1.commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.Personal -> {
                   // Toast.makeText(applicationContext,"Personal Coming Soon ...",Toast.LENGTH_SHORT).show()
                    val seller_personal_fragment = SellerPersonalFragment()
                    val fragmentTransaction1 =
                        supportFragmentManager.beginTransaction()
                    fragmentTransaction1.replace(R.id.seller_frame_content, seller_personal_fragment)
                    fragmentTransaction1.commit()
                    return@OnNavigationItemSelectedListener true

                }
                R.id.Support -> {
                   // Toast.makeText(applicationContext,"Setting Coming Soon ...",Toast.LENGTH_SHORT).show()
                    val seller_support_fragment = SellerSupportFragment()
                    val fragmentTransaction3 =
                        supportFragmentManager.beginTransaction()
                    fragmentTransaction3.replace(R.id.seller_frame_content, seller_support_fragment)
                    fragmentTransaction3.commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_navigation)

        //Attach Listener To Bottom Navigation
        seller_bottom_nav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        //Default Fragment To Preview On Home
        val seller_select_category_fragment = SellerSelectCategoryFragment()
        val fragmentTransaction1 =
            supportFragmentManager.beginTransaction()
        fragmentTransaction1.replace(R.id.seller_frame_content, seller_select_category_fragment, "SellerSelectCategory")
        fragmentTransaction1.commit()

    }
}