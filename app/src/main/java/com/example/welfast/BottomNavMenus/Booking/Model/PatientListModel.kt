package com.example.welfast.BottomNavMenus.Booking.Model

import com.google.gson.annotations.SerializedName

data class PatientListModel(
    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var data    : ArrayList<DataPatientList> = arrayListOf()
)
