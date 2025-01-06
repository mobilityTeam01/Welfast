package com.example.welfast.NavDrawerMenus.EditProfile.PatientDetailsApiModel

import com.google.gson.annotations.SerializedName

data class PatientDetailsData(
    @SerializedName("id"                    ) var id                    : Int?     = null,
    @SerializedName("mrn"                   ) var mrn                   : String?  = null,
    @SerializedName("name"                  ) var name                  : String?  = null,
    @SerializedName("gender"                ) var gender                : String?  = null,
    @SerializedName("dob"                   ) var dob                   : String?  = null,
    @SerializedName("bloodGroup"            ) var bloodGroup            : String?  = null,
    @SerializedName("emailId"               ) var emailId               : String?  = null,
    @SerializedName("contactNo"             ) var contactNo             : String?  = null,
    @SerializedName("address"               ) var address               : Address? = Address(),
    @SerializedName("assignedDoctorId"      ) var assignedDoctorId      : Int?     = null,
    @SerializedName("lastVisit"             ) var lastVisit             : String?  = null,
    @SerializedName("age"                   ) var age                   : String?  = null,
    @SerializedName("lastname"              ) var lastname              : String?  = null,
    @SerializedName("clinicId"              ) var clinicId              : Int?     = null,
    @SerializedName("diabetes"              ) var diabetes              : Boolean? = null,
    @SerializedName("externalPatientStatus" ) var externalPatientStatus : Int?     = null,
    @SerializedName("clinicFileId"          ) var clinicFileId          : String?  = null,
    @SerializedName("relationship"          ) var relationship          : String?  = null,
    @SerializedName("profilePic"            ) var profilePic          : String?  = null
)
