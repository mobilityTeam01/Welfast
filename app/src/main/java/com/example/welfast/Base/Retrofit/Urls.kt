package com.example.welfast.Base.Retrofit

class Urls {
    companion object {

        //New URLS
        //(Development server)
        const  val BASE ="http://208.109.14.7:81/api/"
        const  val IMAGE_BASE ="http://208.109.14.7:81/api/"


        //(Production server)
//        const  val BASE ="https://eva.srishtis.com/api/"
//        const  val IMAGE_BASE ="https://eva.srishtis.com//public/uploads/photo/"


        //Urls entity

        const val GET_PATIENT_DETAILS = "PatientDetails/PatientById"
        const val GET_PATIENT_LIST = "PatientDetails/contactNo"
        const val DOCTORS_LIST = "Doctor/doctors"
        const val EDIT_PROFILE = "PatientDetails/EditProfile"
        const val MEDICAL_REPORT = "MedicalReport"
        const val HOME_API = "Home/homeview"
        const val GET_PACKAGES = "Packages/GetPackages"
        const val GET_NOTIFICATION = "Notification/Notifications"

    }


}