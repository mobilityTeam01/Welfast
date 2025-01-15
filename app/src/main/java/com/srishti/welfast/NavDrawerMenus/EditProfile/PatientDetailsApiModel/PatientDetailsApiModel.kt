package com.srishti.welfast.NavDrawerMenus.EditProfile.PatientDetailsApiModel

import com.google.gson.annotations.SerializedName

data class PatientDetailsApiModel(

    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : PatientDetailsData?   = PatientDetailsData()

)
