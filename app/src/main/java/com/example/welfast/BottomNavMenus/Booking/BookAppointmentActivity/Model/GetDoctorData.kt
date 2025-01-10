package com.example.welfast.BottomNavMenus.Booking.BookAppointmentActivity.Model

import com.google.gson.annotations.SerializedName

data class GetDoctorData(
    @SerializedName("doctorId" ) var doctorId : Int?    = null,
    @SerializedName("name"     ) var name     : String? = null
)
