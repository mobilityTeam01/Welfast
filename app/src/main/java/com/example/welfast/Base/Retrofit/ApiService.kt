package com.example.welfast.Base.Retrofit

import com.example.welfast.BottomNavMenus.Booking.BookAppointmentActivity.Model.AppointmentModel
import com.example.welfast.BottomNavMenus.Booking.Model.PatientListModel
import com.example.welfast.BottomNavMenus.Doctors.DoctorsListModel.DoctorsListModel
import com.example.welfast.BottomNavMenus.HealthPackage.Models.HealthPackageModel
import com.example.welfast.BottomNavMenus.Home.InnerActivities.MedicalReport.Model.MedicalReportModel
import com.example.welfast.BottomNavMenus.Home.Model.HomeModel
import com.example.welfast.Login.Login.Model.OtpModel
import com.example.welfast.NavDrawerMenus.EditProfile.EditProfileApiModel.EditProfileApiModel
import com.example.welfast.NavDrawerMenus.EditProfile.PatientDetailsApiModel.PatientDetailsApiModel
import com.example.welfast.NavDrawerMenus.Notification.Models.NotificationModel
import java.util.concurrent.TimeUnit
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface ApiService {

    @FormUrlEncoded
    @POST(Urls.MEDICAL_REPORT)
    suspend fun getMedicalReport(@FieldMap params: HashMap<String?, String?>)
            : MedicalReportModel

    @FormUrlEncoded
    @POST(Urls.HOME_API)
    suspend fun getHomeDetails(@FieldMap params: HashMap<String?, String?>)
            : HomeModel

    @FormUrlEncoded
    @POST(Urls.GET_PATIENT_DETAILS)
    suspend fun getPatientDetails(@FieldMap params: HashMap<String?, String?>)
            : PatientDetailsApiModel

    @Multipart
    @POST(Urls.EDIT_PROFILE)
    suspend fun editProfile(
        @Part attachment: MultipartBody.Part?,
        @PartMap params: HashMap<String?, RequestBody?>
    ): EditProfileApiModel

    @GET(Urls.DOCTORS_LIST)
    suspend fun getDoctors()
            : DoctorsListModel

    @GET(Urls.GET_PACKAGES)
    suspend fun getPackages()
            : HealthPackageModel

    @FormUrlEncoded
    @POST(Urls.GET_NOTIFICATION)
    suspend fun getNotifications(@FieldMap params: HashMap<String?, String?>)
            : NotificationModel

    @FormUrlEncoded
    @POST(Urls.GET_PATIENT_LIST)
    suspend fun getPatientList(@FieldMap params: HashMap<String?, String?>)
            : PatientListModel

    @FormUrlEncoded
    @POST(Urls.BOOK_APPOINTMENT)
    suspend fun bookAppointment(@FieldMap params: HashMap<String?, String?>)
            : AppointmentModel

    @FormUrlEncoded
    @POST(Urls.SEND_OTP)
    suspend fun sendOtp(@FieldMap params: HashMap<String?, String?>)
            : OtpModel

    companion object {
        private val interceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        private val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(interceptor) // same for .addInterceptor(...)
            .connectTimeout(30, TimeUnit.SECONDS) //Backend is really slow
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
//            .connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
//            .protocols(listOf(Protocol.HTTP_1_1))
            .build()

        operator fun invoke(): ApiService {
            return Retrofit.Builder()
                .baseUrl(Urls.BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ApiService::class.java)
        }


    }
}