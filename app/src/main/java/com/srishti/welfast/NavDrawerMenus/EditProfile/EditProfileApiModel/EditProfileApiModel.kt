package com.srishti.welfast.NavDrawerMenus.EditProfile.EditProfileApiModel

import com.google.gson.annotations.SerializedName

data class EditProfileApiModel(
    @SerializedName("status"  ) var status  : Boolean? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : EditProfileApiData?   = EditProfileApiData()

)
