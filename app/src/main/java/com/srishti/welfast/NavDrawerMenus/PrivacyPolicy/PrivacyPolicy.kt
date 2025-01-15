package com.srishti.welfast.NavDrawerMenus.PrivacyPolicy

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.srishti.welfast.R
import com.srishti.welfast.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicy : AppCompatActivity() {
    lateinit var binding:ActivityPrivacyPolicyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= DataBindingUtil.setContentView(this,R.layout.activity_privacy_policy)

        binding.ivBackButton.ivBack.setOnClickListener { finish() }
    }
}