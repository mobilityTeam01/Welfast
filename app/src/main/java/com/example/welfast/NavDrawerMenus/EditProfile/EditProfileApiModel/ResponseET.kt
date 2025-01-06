package com.example.welfast.NavDrawerMenus.EditProfile.EditProfileApiModel

import com.google.gson.annotations.SerializedName

data class ResponseET(
    @SerializedName("data"    ) var data    : EditProfileApiData?   = EditProfileApiData()
)
