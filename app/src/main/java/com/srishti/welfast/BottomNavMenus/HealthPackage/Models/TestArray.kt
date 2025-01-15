package com.srishti.welfast.BottomNavMenus.HealthPackage.Models

import com.google.gson.annotations.SerializedName

data class TestArray(
    @SerializedName("diagnosticTestId" ) var diagnosticTestId : Int?    = null,
    @SerializedName("diagnosticTest"   ) var diagnosticTest   : String? = null,
    @SerializedName("price"            ) var price            : String? = null,
    @SerializedName("value"            ) var value            : String? = null,
    @SerializedName("unit"             ) var unit             : String? = null
)
