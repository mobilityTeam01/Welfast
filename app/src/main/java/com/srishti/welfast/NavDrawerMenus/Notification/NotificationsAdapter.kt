package com.srishti.welfast.NavDrawerMenus.Notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.srishti.welfast.NavDrawerMenus.Notification.Models.Notifications
import com.srishti.welfast.R

class NotificationsAdapter(private var notificationsList: ArrayList<Notifications>?, private val listener : ItemClickListener):
    RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>(){

    private var notifications:ArrayList<Notifications>?=notificationsList

    open interface ItemClickListener {
        fun itemListClick(notificationName: String?, notificationId: Int?,notificationPic: String?,content: String?,visitingTime:String?)
    }
    companion object {
        var mClickListener: ItemClickListener? = null
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationViewHolder {
        mClickListener =listener
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notifications_adapter, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications?.get(position)

        holder.content.text = notification?.notificationMessage
        holder.heading.text = notification?.notificationHeading
        holder.tvTime.text = notification?.notificationDateTime

        holder.llNotification.setOnClickListener(View.OnClickListener {
            mClickListener?.itemListClick(
                notification?.notificationHeading, notification?.id,notification?.notificationImage,notification?.notificationMessage,notification?.notificationDateTime
            )
        })
    }

    override fun getItemCount(): Int {
        return notifications?.size!!
    }

    class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.img)
        val llNotification: LinearLayout = view.findViewById(R.id.llNotification)

        val newButton: TextView = view.findViewById(R.id.newButton)
        val heading: TextView = view.findViewById(R.id.heading)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val content: TextView = view.findViewById(R.id.content)
    }

}