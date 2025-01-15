package com.srishti.welfast.BottomNavMenus.Home.InnerActivities.MedicalReport.Model

import com.google.gson.annotations.SerializedName

data class MedicalReportData(
    @SerializedName("doctorName"   ) var doctorName   : String?                 = null,
    @SerializedName("doctorNote"   ) var doctorNote   : String?                 = null,
    @SerializedName("visitDate"    ) var visitDate    : String?                 = null,
    @SerializedName("prescription"     ) var prescription     : ArrayList<Prescription> = arrayListOf()
)
