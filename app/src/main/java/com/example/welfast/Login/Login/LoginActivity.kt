package com.example.welfast.Login.Login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.welfast.Base.BaseActivity
import com.example.welfast.Base.Retrofit.ApiService
import com.example.welfast.R
import com.example.welfast.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : BaseActivity() {
    lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=DataBindingUtil.setContentView(this,R.layout.activity_login)

        setClicks()
    }

    private fun setClicks() {
        binding.sendOtpButton.setOnClickListener {
            if (validatePhone(binding.etPhoneNumber)){
                callSendOtpApi()
            }
        }

        binding.loginButton.setOnClickListener {
            callVerifyOtpApi()
        }
    }

    private fun callVerifyOtpApi() {
        showLoadingIndicator(false)
        val params = HashMap<String?, String?>()
        params["phoneNumber"] = binding.etPhoneNumber.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.invoke().verifyOtp(params)
                withContext(Dispatchers.Main) {
                    if (response.status == true) {
                        hideLoadingIndicator()
                    } else {
                        hideLoadingIndicator()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoadingIndicator()
                    Toast.makeText(this@LoginActivity, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun callSendOtpApi() {
        Log.e("callSendOtpApi","CALL")

        showLoadingIndicator(false)
        val params = HashMap<String?, String?>()
        params["phoneNumber"] = binding.etPhoneNumber.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.invoke().sendOtp(params)
                withContext(Dispatchers.Main) {
                    if (response.status == true) {
                        hideLoadingIndicator()
                        Toast.makeText(this@LoginActivity, response.message, Toast.LENGTH_SHORT).show()
                        setVerifyOtp()
                    } else {
                        hideLoadingIndicator()
                        Toast.makeText(this@LoginActivity, response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoadingIndicator()
                    Toast.makeText(this@LoginActivity, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setVerifyOtp() {
        binding.etPhoneNumber.isEnabled=false
        binding.llOtp.visibility= View.VISIBLE
        binding.sendOtpButton.visibility= View.GONE
    }

    private fun validatePhone(phoneNumber: EditText):Boolean {
        if (phoneNumber.equals("")) {
            Toast.makeText(this@LoginActivity,"Enter phone number",Toast.LENGTH_SHORT).show()
            return false
        } else if (phoneNumber.length() < 10) {
            Toast.makeText(this@LoginActivity,"Enter valid phone number",Toast.LENGTH_SHORT).show()

            return false
        }
        return true
    }
}