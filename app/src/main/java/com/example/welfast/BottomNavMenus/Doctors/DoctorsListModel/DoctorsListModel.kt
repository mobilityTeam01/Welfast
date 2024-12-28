package com.example.welfast.BottomNavMenus.Doctors.DoctorsListModel

import com.google.gson.annotations.SerializedName

data class DoctorsListModel(

    @SerializedName("status"  ) var status  : Boolean?        = null,
    @SerializedName("message" ) var message : String?         = null,
    @SerializedName("data"    ) var doctorsList    : ArrayList<DoctorsListData> = arrayListOf()
)
