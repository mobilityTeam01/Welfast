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
                intentActivity(LoginActivity())
//            }
        }, 2000)
    }
}