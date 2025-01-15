package com.srishti.welfast.Login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.databinding.DataBindingUtil
import com.srishti.welfast.Base.BaseActivity
import com.srishti.welfast.Base.Constance
import com.srishti.welfast.Base.PreferenceHelper
import com.srishti.welfast.Dashboard.DashboardActivity
import com.srishti.welfast.Login.Login.LoginActivity
import com.srishti.welfast.R
import com.srishti.welfast.databinding.ActivitySplashScreenBinding

class SplashScreen : BaseActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding=DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)

        setIntentSplash()
    }

    private fun setIntentSplash() {
        Handler(Looper.getMainLooper()).postDelayed({
//            if (PreferenceHelper.readBool(Constance.IS_LOGGED_IN)==true){
                intentActivity(DashboardActivity())
                PreferenceHelper.write(Constance.PATIENT_ID,"A1035")
                PreferenceHelper.write(Constance.CONTACT_NUMBER,"9605736882")
                PreferenceHelper.write(Constance.NAME,"Rahul")
                PreferenceHelper.write(Constance.EMAIL,"rahul@gmail.com")
                PreferenceHelper.write(Constance.USER_PICTURE,"Uploads/ProfilePictures/d5653099-0b9a-40a2-ac76-1b1f6c048ef4.jfif")
                PreferenceHelper.write(Constance.TOKEN," eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImp0aSI6IjJiN2U4MDQxLWYwMDgtNDI1OC04NTBiLTkyNWNkNWYxNTI2NyIsImlzcyI6IldlbGZhc3RBcGkiLCJhdWQiOiJXZWxmYXN0QXBpIn0.jpzVMYbpqwvovZlWzN6nbl5XE42lzJkCY5HY_qG1Mz4")
//            }else{
//                intentActivity(LoginActivity())
//            }
        }, 2000)
    }
}