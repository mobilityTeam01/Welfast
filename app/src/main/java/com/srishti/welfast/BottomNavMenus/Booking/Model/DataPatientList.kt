package com.srishti.welfast.BottomNavMenus.Booking.Model

import com.google.gson.annotations.SerializedName

data class DataPatientList(
    @SerializedName("id"           ) var id           : Int?    = null,
    @SerializedName("name"         ) var name         : String? = null,
    @SerializedName("age"          ) var age          : String? = null,
    @SerializedName("opid"         ) var opid         : String? = null,
    @SerializedName("gender"       ) var gender       : String? = null,
    @SerializedName("relationship" ) var relationship : String? = null

)
