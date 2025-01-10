package com.example.welfast.BottomNavMenus.Doctors.PatientListActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welfast.Base.BaseActivity
import com.example.welfast.Base.Constance
import com.example.welfast.Base.PreferenceHelper
import com.example.welfast.Base.Retrofit.ApiService
import com.example.welfast.BottomNavMenus.Booking.BookAppointmentActivity.BookAppointmentActivity
import com.example.welfast.BottomNavMenus.Booking.Model.DataPatientList
import com.example.welfast.BottomNavMenus.Booking.Model.PatientListModel
import com.example.welfast.BottomNavMenus.Booking.PatientListAdapter
import com.example.welfast.BottomNavMenus.Home.HomeFragment
import com.example.welfast.R
import com.example.welfast.databinding.ActivityPatientListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PatientListActivity : BaseActivity() {
    lateinit var binding:ActivityPatientListBinding
    var patientList=ArrayList<DataPatientList>()
    private var patientListAdapter:PatientListAdapter?=null

    lateinit var from:String
    lateinit var doctorsName:String
    lateinit var doctorsId:String
    lateinit var specialization:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=DataBindingUtil.setContentView(this,R.layout.activity_patient_list)

        val intent = intent

        from = intent.getStringExtra("from").toString()
        doctorsName = intent.getStringExtra("doctorsName").toString()
        doctorsId = intent.getStringExtra("doctorsId").toString()
        specialization = intent.getStringExtra("specialization").toString()

        getPatientListApi()
        setClicks()
    }

    private fun setClicks() {
        binding.ivBackButton.ivBack.setOnClickListener { finish() }

        binding.newPatientButton.setOnClickListener {
            val intent = Intent(this@PatientListActivity, BookAppointmentActivity()::class.java)
            intent.putExtra("patient", "new")
            intent.putExtra("from", "doctor")
            intent.putExtra("doctorsName", doctorsName)
            intent.putExtra("doctorsId", doctorsId)
            intent.putExtra("specialization", specialization)
            startActivity(intent)
        }
    }

    private fun getPatientListApi() {

        showLoadingIndicator(false)
        val params = HashMap<String?, String?>()
        params["contactNo"] = PreferenceHelper.read(Constance.CONTACT_NUMBER)
        val authToken = "Bearer"+ PreferenceHelper.read(Constance.TOKEN)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.invoke().getPatientList(authToken,params)
                withContext(Dispatchers.Main) {
                    if (response.status == true) {
                        hideLoadingIndicator()
                        patientList.addAll(response.data)
                        setData(response)
                    } else {
                        hideLoadingIndicator()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoadingIndicator()
                    Toast.makeText(this@PatientListActivity, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setData(response: PatientListModel) {

        binding.rvPatients.layoutManager = LinearLayoutManager(this@PatientListActivity)

        patientListAdapter = PatientListAdapter(patientList, object : PatientListAdapter.ItemClickListener {

            override fun itemListClick(
                patientName: String?,
                patientId: String?,
                age: String?,
                relationship: String?,
                opNumber: String?,
                gender:String?
            ) {
                val intent = Intent(this@PatientListActivity, BookAppointmentActivity()::class.java)
                intent.putExtra("from", "doctor")
                intent.putExtra("patient", "old")
                intent.putExtra("patientName", patientName)
                intent.putExtra("patientId", patientId)
                intent.putExtra("age", age)
                intent.putExtra("gender", gender)
                intent.putExtra("relationship", relationship)
                intent.putExtra("opNumber", opNumber)
                intent.putExtra("doctorsName", doctorsName)
                intent.putExtra("doctorsId", doctorsId)
                intent.putExtra("specialization", specialization)
                startActivity(intent)
            }
        })

        binding.rvPatients.adapter = patientListAdapter

    }
}