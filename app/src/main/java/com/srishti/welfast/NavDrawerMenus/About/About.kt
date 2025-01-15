package com.srishti.welfast.NavDrawerMenus.About

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.srishti.welfast.R
import com.srishti.welfast.databinding.ActivityAboutBinding

class About : AppCompatActivity() {
    lateinit var binding:ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= DataBindingUtil.setContentView(this,R.layout.activity_about)

        binding.ivBackButton.ivBack.setOnClickListener { finish() }

    }
}