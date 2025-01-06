package com.example.welfast.NavDrawerMenus.About

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.welfast.R
import com.example.welfast.databinding.ActivityAboutBinding
import com.example.welfast.databinding.ActivityEditProfileBinding

class About : AppCompatActivity() {
    lateinit var binding:ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= DataBindingUtil.setContentView(this,R.layout.activity_about)

        binding.ivBackButton.ivBack.setOnClickListener { finish() }

    }
}