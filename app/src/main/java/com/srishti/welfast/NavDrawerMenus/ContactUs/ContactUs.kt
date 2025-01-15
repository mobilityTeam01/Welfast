package com.srishti.welfast.NavDrawerMenus.ContactUs

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.srishti.welfast.R
import com.srishti.welfast.databinding.ActivityContactUsBinding

class ContactUs : AppCompatActivity() {
    lateinit var binding:ActivityContactUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding= DataBindingUtil.setContentView(this,R.layout.activity_contact_us)

        binding.ivBackButton.ivBack.setOnClickListener { finish() }
    }
}