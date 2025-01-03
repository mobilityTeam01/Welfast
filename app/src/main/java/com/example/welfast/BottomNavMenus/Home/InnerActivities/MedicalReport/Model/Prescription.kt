package com.example.welfast.BottomNavMenus.Home.InnerActivities.MedicalReport.Model

import com.google.gson.annotations.SerializedName

data class Prescription(
    @SerializedName("brand"    ) var brand    : String? = null,
    @SerializedName("drug"     ) var drug     : String? = null,
    @SerializedName("dosage"   ) var dosage   : String? = null,
    @SerializedName("duration" ) var duration : String? = null,
    @SerializedName("qty"      ) var qty      : Int?    = null,
    @SerializedName("comment"  ) var comment  : String? = null
)
