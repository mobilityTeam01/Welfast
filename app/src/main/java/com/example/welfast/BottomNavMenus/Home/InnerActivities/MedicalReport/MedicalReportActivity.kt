package com.example.welfast.BottomNavMenus.Home.InnerActivities.MedicalReport

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welfast.Base.BaseActivity
import com.example.welfast.Base.Constance
import com.example.welfast.Base.PreferenceHelper
import com.example.welfast.Base.Retrofit.ApiService
import com.example.welfast.BottomNavMenus.Doctors.DoctorsListAdapter
import com.example.welfast.BottomNavMenus.Doctors.DoctorsListModel.DoctorsListData
import com.example.welfast.BottomNavMenus.Doctors.ViewProfile.ViewProfileActivity
import com.example.welfast.BottomNavMenus.Home.InnerActivities.MedicalReport.Adapters.PrescriptionAdapter
import com.example.welfast.BottomNavMenus.Home.InnerActivities.MedicalReport.Model.MedicalReportModel
import com.example.welfast.BottomNavMenus.Home.InnerActivities.MedicalReport.Model.Prescription
import com.example.welfast.R
import com.example.welfast.databinding.ActivityViewProfileBinding
import com.example.welfast.databinding.MedicalReportBinding
import com.example.welfast.databinding.MedicalReportTestBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MedicalReportActivity : BaseActivity() {
    lateinit var binding:MedicalReportBinding

    var prescriptionList=ArrayList<Prescription>()
    private var prescriptionAdapter: PrescriptionAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=DataBindingUtil.setContentView(this,R.layout.medical_report)

        callMedicalReportApi()
        binding.ivBackButton.ivBack.setOnClickListener { finish() }
    }

    private fun callMedicalReportApi() {
        Log.e("callGetProfileDetailsApi","CALL")

        showLoadingIndicator(false)
        val params = HashMap<String?, String?>()
        params["patientId"] = PreferenceHelper.read(Constance.PATIENT_ID)
        val authToken = "Bearer"+ PreferenceHelper.read(Constance.TOKEN)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.invoke().getMedicalReport(authToken,params)
                withContext(Dispatchers.Main) {
                    if (response.status == true) {
                        hideLoadingIndicator()
                        prescriptionList.addAll(response.data!!.prescription)
                        setData(response)
                    } else {
                        hideLoadingIndicator()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoadingIndicator()
                    Toast.makeText(this@MedicalReportActivity, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setData(response: MedicalReportModel) {

        binding.rvPrescription.layoutManager = LinearLayoutManager(this)

        prescriptionAdapter = PrescriptionAdapter(prescriptionList, object : PrescriptionAdapter.ItemClickListener {

            override fun itemListClick(drug: String?, dosage: String?) {
                //To open dosage popup
            }
        })
        binding.rvPrescription.adapter = prescriptionAdapter


        binding.doctorName.text=    getString(R.string.docName)+response.data?.doctorName
        binding.tvDate.text=        getString(R.string.visitDate)+response.data?.appointmentDate
        binding.tvPatientName.text= getString(R.string.patientName)+response.data?.name
        binding.tvAge.text=         getString(R.string.ageMR)+response.data?.age
        binding.tvGender.text=      getString(R.string.genderMR)+response.data?.gender

    }
}