package com.example.welfast.BottomNavMenus.Home.InnerActivities.MedicalReport.Model

import com.google.gson.annotations.SerializedName

data class MedicalReportData(
    @SerializedName("id"               ) var id               : Int?                    = null,
    @SerializedName("mrn"              ) var mrn              : String?                 = null,
    @SerializedName("name"             ) var name             : String?                 = null,
    @SerializedName("gender"           ) var gender           : String?                 = null,
    @SerializedName("assignedDoctorId" ) var assignedDoctorId : Int?                    = null,
    @SerializedName("age"              ) var age              : String?                 = null,
    @SerializedName("appointmentDate"  ) var appointmentDate  : String?                 = null,
    @SerializedName("doctorName"       ) var doctorName       : String?                 = null,
    @SerializedName("diagnostics"      ) var diagnostics      : ArrayList<Diagnostics>  = arrayListOf(),
    @SerializedName("labResults"       ) var labResults       : ArrayList<String>       = arrayListOf(),
    @SerializedName("prescription"     ) var prescription     : ArrayList<Prescription> = arrayListOf()
)
