package com.example.welfast.BottomNavMenus.Doctors.ViewProfile

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.welfast.Base.BaseActivity
import com.example.welfast.R
import com.example.welfast.databinding.ActivityViewProfileBinding

class ViewProfileActivity : BaseActivity() {
    lateinit var binding:ActivityViewProfileBinding
    lateinit var doctorsName:String
    lateinit var degree:String
    lateinit var profilePic:String
    lateinit var specialization:String
    lateinit var visitingTime:String
    var doctorsId:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=DataBindingUtil.setContentView(this,R.layout.activity_view_profile)


        val intent: Intent = intent
        doctorsName = intent.getStringExtra("doctorsName").toString()
        doctorsId = intent.getIntExtra("doctorsId", 0)
        degree = intent.getStringExtra("degree").toString()
        profilePic = intent.getStringExtra("profilePic").toString()
        specialization = intent.getStringExtra("specialization").toString()
        visitingTime = intent.getStringExtra("visitingTime").toString()

        binding.ivBackButton.ivBack.setOnClickListener { finish() }

        setData()
    }

    private fun setData() {
            Glide.with(this)
            .load(profilePic)
            .placeholder(R.drawable.circular_profile_pic)
            .into(binding.ivDoctorsProfileImage)

        binding.tvName.text        =doctorsName
        binding.tvDegree.text      =degree
        binding.tvSpecialty.text   =specialization
        binding.tvVisitingTime.text=visitingTime
    }
}