package com.srishti.welfast.Login.Login

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.databinding.DataBindingUtil
import com.srishti.welfast.Base.BaseActivity
import com.srishti.welfast.Base.Retrofit.ApiService
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.ozcanalasalvar.otp_view.view.OtpView
import com.srishti.welfast.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import com.srishti.welfast.R

class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var verificationId: String // Firebase verification ID
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    lateinit var otpValue:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.otpView.apply {
            setActiveColor(getColor(R.color.white))
            setDigits(6)
            setAutoFocusEnabled(false)
            setErrorEnabled(false)
            setTextColor(getColor(R.color.black))
            setTextSize(22)
            setTextChangeListener(object : OtpView.ChangeListener {
                override fun onTextChange(value: String, completed: Boolean) {
                    otpValue=value
                }
            })
        }

        setClicks()
    }

    private fun setClicks() {
        binding.sendOtpButton.setOnClickListener {
            if (validatePhone(binding.etPhoneNumber)) {
                callSendOtpApi()
            }
        }

        binding.verifyOtpButton.setOnClickListener {
            if (otpValue.isNotEmpty()) {
                verifyOtp(otpValue)
            } else {
                Toast.makeText(this, "Enter the OTP code", Toast.LENGTH_SHORT).show()
            }
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

                        showPatientProfileDialog()
                    } else {
                        hideLoadingIndicator()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoadingIndicator()
                    Toast.makeText(
                        this@LoginActivity,
                        "An error occurred. Please try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun showPatientProfileDialog() {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_choose_profiles)

            val radioGroupProfiles = dialog.findViewById<RadioGroup>(R.id.radioGroupProfiles)

            radioGroupProfiles.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rb1 -> {

                    }
                    R.id.rb2 -> {

                    }
                    R.id.rb3 -> {

                    }
                }
                dialog.dismiss() // Close dialog after selection
            }

            dialog.show()
        }


    private fun callSendOtpApi() {
        Log.e("callSendOtpApi", "CALL")

//        showLoadingIndicator(false)
//        val params = HashMap<String?, String?>()
//        params["phoneNumber"] = binding.etPhoneNumber.text.toString()
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val response = ApiService.invoke().sendOtp(params)
//                withContext(Dispatchers.Main) {
//                    if (response.status == true) {
//                        hideLoadingIndicator()
//                        Toast.makeText(this@LoginActivity, response.message, Toast.LENGTH_SHORT).show()
                        setVerifyOtp()
                        sendOtpWithFirebase(binding.etPhoneNumber.text.toString()) // Call Firebase OTP function
//                    } else {
//                        hideLoadingIndicator()
//                        Toast.makeText(this@LoginActivity, response.message, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    hideLoadingIndicator()
//                    Toast.makeText(
//                        this@LoginActivity,
//                        "An error occurred. Please try again later.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }
    }

    private fun setVerifyOtp() {
        binding.etPhoneNumber.isEnabled = false
        binding.llOtp.visibility = View.VISIBLE
        binding.sendOtpButton.visibility = View.GONE
    }

    private fun validatePhone(phoneNumber: EditText): Boolean {
        val phone = phoneNumber.text.toString()
        if (phone.isEmpty()) {
            Toast.makeText(this@LoginActivity, "Enter phone number", Toast.LENGTH_SHORT).show()
            return false
        } else if (phone.length < 10) {
            Toast.makeText(this@LoginActivity, "Enter valid phone number", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun sendOtpWithFirebase(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber("+91$phoneNumber") // Add country code before phone number
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Automatically verified
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // Verification failed
                    Toast.makeText(
                        this@LoginActivity,
                        "OTP Verification failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    // OTP Sent successfully
                    this@LoginActivity.verificationId = verificationId
                    resendToken = token
                    Toast.makeText(this@LoginActivity, "OTP Sent", Toast.LENGTH_SHORT).show()
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyOtp(otp: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // OTP Verified
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    callVerifyOtpApi()
                } else {
                    // Verification failed
                    Toast.makeText(this, "OTP Verification failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
