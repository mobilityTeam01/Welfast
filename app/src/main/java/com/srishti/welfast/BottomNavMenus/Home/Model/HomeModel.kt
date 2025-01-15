package com.srishti.welfast.BottomNavMenus.Home.Model

import com.google.gson.annotations.SerializedName

data class  HomeModel(
    @SerializedName("status"        ) var status        : Boolean?           = null,
    @SerializedName("message"       ) var message       : String?            = null,
    @SerializedName("doctors"       ) var doctors       : ArrayList<DoctorsHome> = arrayListOf(),
    @SerializedName("patientReport" ) var patientReport : PatientReportHome?     = PatientReportHome()

)
