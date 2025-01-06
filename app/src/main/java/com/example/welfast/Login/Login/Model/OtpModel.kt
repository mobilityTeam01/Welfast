package com.example.welfast.Login.Login.Model

import com.google.gson.annotations.SerializedName

data class OtpModel(
    @SerializedName("status"        ) var status        : Boolean?           = null,
    @SerializedName("message"       ) var message       : String?            = null
)
