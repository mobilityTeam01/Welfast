package com.srishti.welfast.BottomNavMenus.Booking.BookAppointmentActivity.Model

import com.google.gson.annotations.SerializedName

data class GetDoctorModel(
    @SerializedName("status"  ) var status  : Boolean?           = null,
    @SerializedName("message" ) var message : String?            = null,
    @SerializedName("doctors" ) var doctors : ArrayList<GetDoctorData> = arrayListOf()
)
