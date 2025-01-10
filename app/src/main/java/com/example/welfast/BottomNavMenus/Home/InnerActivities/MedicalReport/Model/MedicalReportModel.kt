package com.example.welfast.BottomNavMenus.Home.InnerActivities.MedicalReport.Model

import com.google.gson.annotations.SerializedName

data class MedicalReportModel(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("patientReport"    ) var patientReport    : MedicalReportData?    = MedicalReportData()
)
