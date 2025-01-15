package com.srishti.welfast.BottomNavMenus.HealthPackage.Models

import com.google.gson.annotations.SerializedName

data class HealthPackageModel(
    @SerializedName("status"         ) var status         : Boolean?                  = null,
    @SerializedName("message"        ) var message        : String?                   = null,
    @SerializedName("diagnosticTest" ) var diagnosticTest : ArrayList<TestArray> = arrayListOf(),
//    @SerializedName("subTests"       ) var subTests       : ArrayList<SubTests>       = arrayListOf(),
//    @SerializedName("sub2Tests"      ) var sub2Tests      : ArrayList<Sub2Tests>      = arrayListOf()
)
