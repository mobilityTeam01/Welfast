package com.srishti.welfast.BottomNavMenus.Booking.BookAppointmentActivity.Model

import com.google.gson.annotations.SerializedName

data class AppointmentModel(
    @SerializedName("status"         ) var status         : Boolean? = null,
    @SerializedName("message"        ) var message        : String?  = null,
    @SerializedName("appointmentId"  ) var appointmentId  : Int?     = null,
    @SerializedName("notificationId" ) var notificationId : Int?     = null
)
