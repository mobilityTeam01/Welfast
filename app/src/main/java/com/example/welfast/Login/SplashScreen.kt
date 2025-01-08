package com.example.welfast.Login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.databinding.DataBindingUtil
import com.example.welfast.Base.BaseActivity
import com.example.welfast.Base.Constance
import com.example.welfast.Base.PreferenceHelper
import com.example.welfast.Dashboard.DashboardActivity
import com.example.welfast.Login.Login.LoginActivity
import com.example.welfast.R
import com.example.welfast.databinding.ActivitySplashScreenBinding

class SplashScreen : BaseActivity() {
    lateinit var binding:ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding=DataBindingUtil.setContentView(this,R.layout.activity_splash_screen)

        setIntentSplash()
    }

    private fun setIntentSplash() {
        Handler(Looper.getMainLooper()).postDelayed({
//            if (PreferenceHelper.readBool(Constance.IS_LOGGED_IN)==true){
//                intentActivity(DashboardActivity())
//            }else{
                intentActivity(DashboardActivity())

            PreferenceHelper.write(Constance.PATIENT_ID,"A1035")
            PreferenceHelper.write(Constance.CONTACT_NUMBER,"9605736882")
            PreferenceHelper.write(Constance.TOKEN,"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImp0aSI6Ijg1MjEyNGQ2LTExMTQtNGVkNC05MTU3LWUyZGU0MjJhZDM5OCIsImV4cCI6MTczNjMxMDM3MiwiaXNzIjoiV2VsZmFzdEFwaSIsImF1ZCI6IldlbGZhc3RBcGkifQ.fRo2uifXwRZnBfgEMRK_V-xX9jOn5HAGGcMqHRLYlTE")
//            }
        }, 2000)
    }
}