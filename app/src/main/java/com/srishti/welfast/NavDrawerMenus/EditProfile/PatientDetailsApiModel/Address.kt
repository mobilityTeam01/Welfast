package com.srishti.welfast.NavDrawerMenus.EditProfile.PatientDetailsApiModel

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("houseNo" ) var houseNo : String? = null,
    @SerializedName("street"  ) var street  : String? = null,
    @SerializedName("city"    ) var city    : String? = null,
    @SerializedName("pinNo"   ) var pinNo   : String? = null
)
