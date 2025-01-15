package com.srishti.welfast.BottomNavMenus.Home.InnerActivities.MedicalReport.Model

import com.google.gson.annotations.SerializedName

data class Diagnostics(
    @SerializedName("diagnostics" ) var diagnostics : String? = null,
    @SerializedName("doctorNote"  ) var doctorNote  : String? = null,
    @SerializedName("bp"          ) var bp          : String? = null,
    @SerializedName("weight"      ) var weight      : String? = null,
    @SerializedName("height"      ) var height      : String? = null,
    @SerializedName("pr"          ) var pr          : String? = null,
    @SerializedName("fbs"         ) var fbs         : String? = null,
    @SerializedName("rbd"         ) var rbd         : String? = null,
    @SerializedName("cr"          ) var cr          : String? = null,
    @SerializedName("ux"          ) var ux          : String? = null,
    @SerializedName("na"          ) var na          : String? = null,
    @SerializedName("kx"          ) var kx          : String? = null,
    @SerializedName("tsh"         ) var tsh         : String? = null,
    @SerializedName("ftu"         ) var ftu         : String? = null
)
