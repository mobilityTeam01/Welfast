package com.example.welfast.NavDrawerMenus.EditProfile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.welfast.Base.BaseActivity
import com.example.welfast.Base.Constance
import com.example.welfast.Base.PreferenceHelper
import com.example.welfast.Base.Retrofit.ApiService
import com.example.welfast.Base.Retrofit.Urls
import com.example.welfast.NavDrawerMenus.EditProfile.PatientDetailsApiModel.PatientDetailsApiModel
import com.example.welfast.R
import com.example.welfast.databinding.ActivityEditProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class EditProfile : BaseActivity() {

    lateinit var binding:ActivityEditProfileBinding
    private val REQUEST_IMAGE_PICK = 100
    var selectedGender="male"

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
            binding=DataBindingUtil.setContentView(this,R.layout.activity_edit_profile)
            //binding=ActivityEditProfileBinding.inflate(layoutInflater)

            setClicks()

            if (isOnline(application)){
                callGetProfileDetailsApi()
            }else{
                Toast.makeText(this@EditProfile, "No Internet Connection", Toast.LENGTH_SHORT).show()
//                intentActivity(NoInternetConnection())
            }

            checkKeyboardVisibility(binding.llView1,binding.vViewUp,binding.llView2)

            initializeTextWatchersET()
    }

    private fun initializeTextWatchersET() {
        addTextChangedListener(binding.etName   )
        addTextChangedListener(binding.etAge    )
        addTextChangedListener(binding.etBlood  )
        addTextChangedListener(binding.etPhone  )
        addTextChangedListener(binding.etAddress)
        addTextChangedListener(binding.etEmail  )
    }

    private fun callGetProfileDetailsApi() {

        Log.e("callGetProfileDetailsApi","CALL")

        showLoadingIndicator(false)
        val params = HashMap<String?, String?>()
        //params["patientId"] = PreferenceHelper.read(Constance.PATIENT_ID)
        params["patientId"] = "A1035"
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.invoke().getPatientDetails(params)
                withContext(Dispatchers.Main) {
                    if (response.status == true) {
                        hideLoadingIndicator()
                        setData(response)
                    } else {
                        hideLoadingIndicator()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoadingIndicator()
                    Toast.makeText(this@EditProfile, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun setData(response: PatientDetailsApiModel) {
        binding.etName.setText(response.data?.name)
        binding.etAge.setText(response.data?.age)
        if (response.data?.gender.equals("male")){
            binding.rgGender.check(R.id.rbMale)
            selectedGender="male"
        }else{
            binding.rgGender.check(R.id.rbFeMale)
            selectedGender="feMale"
        }

        binding.etBlood.setText(response.data?.bloodGroup)
        binding.etPhone.setText(response.data?.contactNo)
        binding.etAddress.setText(response.data?.address?.city)
        binding.etEmail.setText(response.data?.emailId)

//        Glide.with(this)
//            .load(Urls.IMAGE_BASE+PreferenceHelper.read(Constance.USER_PICTURE))
//            .placeholder(R.drawable.circular_profile_pic)
//            .into(binding.imgProfile)
    }

    private fun setClicks() {

        binding.rlProfilePic.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Request permission
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_IMAGE_PICK)
            } else {
                // Permission already granted, open gallery
                openGallery()
            }
        }

        binding.ivBackButton.ivBack.setOnClickListener { finish() }
        binding.saveButton.button.text=getString(R.string.save)
        binding.saveButton.button.setOnClickListener {
            if (validateData()){
                callUpdateProfileDetailsApi()
            }

        }

    }

    private fun validateData(): Boolean {
        if (binding.etName.text.isEmpty()) {
            binding.etName.error = getString(R.string.cannotBeEmpty)
            scroll(binding.etName)
            return false
        }

        if (binding.etName.text.isEmpty()) {
            binding.etName.error = getString(R.string.cannotBeEmpty)
            scroll(binding.etName)
            return false
        }

        if (binding.etAge.text.isEmpty()) {
            binding.etAge.error = getString(R.string.cannotBeEmpty)
            scroll(binding.etAge)
            return false
        }

        if (binding.etBlood.text.isEmpty()) {
            binding.etBlood.error = getString(R.string.cannotBeEmpty)
            scroll(binding.etBlood)
            return false
        }

        if (binding.etPhone.text.isEmpty()) {
            binding.etPhone.error = getString(R.string.cannotBeEmpty)
            scroll(binding.etPhone)
            return false
        }

        if (binding.etAddress.text.isEmpty()) {
            binding.etAddress.error = getString(R.string.cannotBeEmpty)
            scroll(binding.etAddress)
            return false
        }

        if (binding.etEmail.text.isEmpty()) {
            binding.etEmail.error = getString(R.string.cannotBeEmpty)
            scroll(binding.etEmail)
            return false
        } else if (binding.etEmail.text.isNotEmpty()) {
            if (!isEmailValid(binding.etEmail.text.toString())) {
                binding.etEmail.error = getString(R.string.validEmail)
                scroll(binding.etEmail)
                return false
            }
        }

        // All inputs are valid
        return true
    }

    private fun scroll(targetEditText: EditText) {

        binding.scrollView.post {
            binding.scrollView.smoothScrollTo(0, targetEditText.top)
        }
        targetEditText.requestFocus()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_IMAGE_PICK) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open gallery
                openGallery()
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                // Set the image on the ImageView
                binding.imgProfile.setImageURI(uri)
            }
        }
    }

    private fun callUpdateProfileDetailsApi() {
        showLoadingIndicator(false)
        CoroutineScope(Dispatchers.IO).launch {
            try {
//                val file: File? = if (imageUri != null) {
//                    val filesDir = requireContext().filesDir
//                    val file = File(filesDir, "image.jpg")
//
//                    val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
//                    val outputStream = FileOutputStream(file)
//                    inputStream?.copyTo(outputStream)
//
//                    file
//                } else {
//                    null
//                }

//                val requestFile: RequestBody? = file?.let {
//                    it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
//                }
//
//                val multipartBody: MultipartBody.Part? = requestFile?.let {
//                    MultipartBody.Part.createFormData("photo", file?.name, it)
//                }

                //remove this line
                val file=null

                val requestFile: RequestBody? = file?.let {
                    it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                }

                val multipartBody: MultipartBody.Part? = requestFile?.let {
                    MultipartBody.Part.createFormData("photo", null.toString())
                }

                //val userid = RequestBody.create(MultipartBody.FORM, PrefsHelper.read(Constance.USER_ID).toString())
                val name = RequestBody.create(MultipartBody.FORM, binding.etName.text.toString())
                val age = RequestBody.create(MultipartBody.FORM, binding.etAge.text.toString())
                val gender = RequestBody.create(MultipartBody.FORM, selectedGender)
                val contactNumber = RequestBody.create(MultipartBody.FORM, binding.etPhone.text.toString())
                val bloodGroup = RequestBody.create(MultipartBody.FORM, binding.etBlood.text.toString())
                val address = RequestBody.create(MultipartBody.FORM, binding.etAddress.text.toString())

                val map = HashMap<String?, RequestBody?>()
                //map["user_id"] = userid
                map["Name"] = name
                map["Age"] = age
                map["Gender"] = gender
                map["ContactNo"] = contactNumber
                map["BloodGroup"] = bloodGroup
                map["Address"] = address

                val response = ApiService.invoke().editProfile(multipartBody, map)

                withContext(Dispatchers.Main) {
                    if (response.status == true) {
                        hideLoadingIndicator()
                        removeFocus()
                        Toast.makeText(this@EditProfile, response.message, Toast.LENGTH_SHORT).show()
                        finish()

                    } else {
                        hideLoadingIndicator()
                        Toast.makeText(this@EditProfile, response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoadingIndicator()
                    Toast.makeText(this@EditProfile, "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun removeFocus() {
        Log.e("FOCUS","remove")
        binding.etName.clearFocus()
        binding.etAge.clearFocus()
        binding.etBlood.clearFocus()
        binding.etPhone.clearFocus()
        binding.etAddress.clearFocus()
        binding.etEmail.clearFocus()
    }
}