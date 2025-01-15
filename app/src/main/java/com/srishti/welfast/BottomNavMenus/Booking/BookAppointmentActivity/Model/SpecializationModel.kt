package com.srishti.welfast.BottomNavMenus.Booking.BookAppointmentActivity.Model

import com.google.gson.annotations.SerializedName

data class SpecializationModel(
    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<SpecializationData> = arrayListOf()
)
