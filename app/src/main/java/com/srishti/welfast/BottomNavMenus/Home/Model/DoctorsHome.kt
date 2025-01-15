package com.srishti.welfast.BottomNavMenus.Home.Model

import com.google.gson.annotations.SerializedName

data class DoctorsHome(
    @SerializedName("doctorId"       ) var doctorId       : Int?    = null,
    @SerializedName("name"           ) var name           : String? = null,
    @SerializedName("specialization" ) var specialization : String? = null,
    @SerializedName("profilePic"     ) var profilePic     : String? = null,
    @SerializedName("visitingTime"   ) var visitingTime   : String? = null
)
