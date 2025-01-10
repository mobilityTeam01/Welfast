package com.example.welfast.BottomNavMenus.Home.Model

import com.google.gson.annotations.SerializedName

data class PatientReportHome(
    @SerializedName("doctorName"   ) var doctorName   : String?                 = null,
    @SerializedName("visitDate"    ) var visitDate    : String?                 = null,
    @SerializedName("doctorNote"   ) var doctorNote   : String?                 = null,
//    @SerializedName("labResults"   ) var labResults   : ArrayList<LabResults>   = arrayListOf(),
    @SerializedName("prescription" ) var prescription : ArrayList<PrescriptionHome> = arrayListOf()
)
