package com.example.welfast.NavDrawerMenus.EditProfile.EditProfileApiModel

import com.google.gson.annotations.SerializedName

data class EditProfileApiData(
    @SerializedName("id"         ) var id         : Int?    = null,
    @SerializedName("name"       ) var name       : String? = null,
    @SerializedName("age"        ) var age        : String? = null,
    @SerializedName("gender"     ) var gender     : String? = null,
    @SerializedName("contactNo"  ) var contactNo  : String? = null,
    @SerializedName("bloodGroup" ) var bloodGroup : String? = null,
    @SerializedName("profilePic" ) var profilePic : String? = null,
    @SerializedName("city"       ) var city       : String? = null
)
