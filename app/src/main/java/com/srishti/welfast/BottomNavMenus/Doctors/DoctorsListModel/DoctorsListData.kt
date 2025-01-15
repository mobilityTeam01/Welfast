package com.srishti.welfast.BottomNavMenus.Doctors.DoctorsListModel

import com.google.gson.annotations.SerializedName

data class DoctorsListData(
    @SerializedName("doctorId"       ) var doctorId       : Int?    = null,
    @SerializedName("name"           ) var name           : String? = null,
    @SerializedName("specialization" ) var specialization : String? = null,
    @SerializedName("profilePic"     ) var profilePic     : String? = null,
    @SerializedName("visitingTime"   ) var visitingTime   : String? = null,
    @SerializedName("degree"         ) var degree         : String? = null
)
