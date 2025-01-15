package com.srishti.welfast.BottomNavMenus.Home.InnerActivities.MedicalReport

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.srishti.welfast.Base.BaseActivity
import com.srishti.welfast.Base.Constance
import com.srishti.welfast.Base.PreferenceHelper
import com.srishti.welfast.Base.Retrofit.ApiService
import com.srishti.welfast.BottomNavMenus.Home.InnerActivities.MedicalReport.Adapters.PrescriptionAdapter
import com.srishti.welfast.BottomNavMenus.Home.InnerActivities.MedicalReport.Model.MedicalReportModel
import com.srishti.welfast.BottomNavMenus.Home.InnerActivities.MedicalReport.Model.Prescription
import com.srishti.welfast.R
import com.srishti.welfast.databinding.MedicalReportBinding
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
                        response.patientReport?.let { prescriptionList.addAll(it.prescription) }
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


        binding.doctorName.text=    getString(R.string.docName)+response.patientReport?.doctorName
        binding.tvDate.text=        getString(R.string.visitDate)+response.patientReport?.visitDate
//        binding.tvPatientName.text= getString(R.string.patientName)+response.patientReport?.
//        binding.tvAge.text=         getString(R.string.ageMR)+response.patientReport?
//        binding.tvGender.text=      getString(R.string.genderMR)+response.patientReport?.gender

    }
}