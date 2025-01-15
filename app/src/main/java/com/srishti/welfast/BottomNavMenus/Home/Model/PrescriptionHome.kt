package com.srishti.welfast.BottomNavMenus.Home.Model

import com.google.gson.annotations.SerializedName

data class PrescriptionHome(
    @SerializedName("slNo"     ) var slNo     : Int?    = null,
    @SerializedName("brand"    ) var brand    : String? = null,
    @SerializedName("drug"     ) var drug     : String? = null,
    @SerializedName("dosage"   ) var dosage   : String? = null,
    @SerializedName("duration" ) var duration : String? = null,
    @SerializedName("qty"      ) var qty      : Int?    = null,
    @SerializedName("comment"  ) var comment  : String? = null
)
