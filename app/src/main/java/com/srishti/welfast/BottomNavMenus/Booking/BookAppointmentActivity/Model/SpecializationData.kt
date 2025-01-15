package com.srishti.welfast.BottomNavMenus.Booking.BookAppointmentActivity.Model

import com.google.gson.annotations.SerializedName

data class SpecializationData(
    @SerializedName("id"             ) var id             : Int?    = null,
    @SerializedName("specialization" ) var specialization : String? = null
)
