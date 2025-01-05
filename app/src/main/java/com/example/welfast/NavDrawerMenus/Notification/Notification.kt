package com.example.welfast.NavDrawerMenus.Notification

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welfast.Base.BaseActivity
import com.example.welfast.Base.Retrofit.ApiService
import com.example.welfast.BottomNavMenus.Doctors.ViewProfile.ViewProfileActivity
import com.example.welfast.BottomNavMenus.Home.DoctorsListHomeAdapter
import com.example.welfast.BottomNavMenus.Home.Model.DoctorsHome
import com.example.welfast.NavDrawerMenus.Notification.Models.NotificationModel
import com.example.welfast.NavDrawerMenus.Notification.Models.Notifications
import com.example.welfast.R
import com.example.welfast.databinding.ActivityNotificationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Notification : BaseActivity() {
    lateinit var binding:ActivityNotificationBinding

    var notificationsList=ArrayList<Notifications>()
    private var notificationsListAdapter: NotificationsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=DataBindingUtil.setContentView(this,R.layout.activity_notification)


        binding.ivBackButton.ivBack.setOnClickListener { finish() }

        getNotificationsApi()

    }

    private fun getNotificationsApi() {

        showLoadingIndicator(false)
        val params = HashMap<String?, String?>()
        //params["patientId"] = PreferenceHelper.read(Constance.PATIENT_ID)
        params["patientId"] = "100"
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.invoke().getNotifications(params)
                withContext(Dispatchers.Main) {
                    if (response.status == true) {
                        hideLoadingIndicator()
                        notificationsList.addAll(response.notifications)
                        setData(response)
                    } else {
                        hideLoadingIndicator()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoadingIndicator()
                    Toast.makeText(this@Notification, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setData(response: NotificationModel) {
        if (response.notifications.size==0){
            binding.noNotifications.visibility= View.VISIBLE
            binding.rvNotifications.visibility= View.GONE
        }else{
            binding.noNotifications.visibility= View.GONE
            binding.rvNotifications.visibility= View.VISIBLE
        }
        binding.rvNotifications.layoutManager = LinearLayoutManager(this)

        notificationsListAdapter = NotificationsAdapter(notificationsList, object : NotificationsAdapter.ItemClickListener {

            override fun itemListClick(
                doctorsName: String?,
                doctorsId: Int?,
                profilePic: String?,
                specialization: String?,
                visitingTime: String?
            ) {
                //TODO
            }
        })

        binding.rvNotifications.adapter = notificationsListAdapter

    }
}