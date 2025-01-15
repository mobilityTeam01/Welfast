package com.srishti.welfast.NavDrawerMenus.Notification.Models

import com.google.gson.annotations.SerializedName

data class Notifications(
    @SerializedName("id"                   ) var id                   : Int?    = null,
    @SerializedName("notificationMessage"  ) var notificationMessage  : String? = null,
    @SerializedName("notificationHeading"  ) var notificationHeading  : String? = null,
    @SerializedName("notificationImage"    ) var notificationImage    : String? = null,
    @SerializedName("notificationDateTime" ) var notificationDateTime : String? = null
)
