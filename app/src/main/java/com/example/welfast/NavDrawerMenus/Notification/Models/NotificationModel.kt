package com.example.welfast.NavDrawerMenus.Notification.Models

import com.google.gson.annotations.SerializedName

data class NotificationModel(
    @SerializedName("status"        ) var status        : Boolean?                 = null,
    @SerializedName("message"       ) var message       : String?                  = null,
    @SerializedName("notifications" ) var notifications : ArrayList<Notifications> = arrayListOf()

)
