package com.example.welfast.BottomNavMenus.Doctors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.welfast.Base.Retrofit.Urls
import com.example.welfast.BottomNavMenus.Doctors.DoctorsListModel.DoctorsListData
import com.example.welfast.BottomNavMenus.Doctors.DoctorsListModel.DoctorsListModel
import com.example.welfast.R

class DoctorsListAdapter(private var doctorsList: ArrayList<DoctorsListData>?, private val listener : ItemClickListener):
    RecyclerView.Adapter<DoctorsListAdapter.DoctorViewHolder>() {

    open interface ItemClickListener {
        fun viewProfile(doctorsName: String?, doctorsId: Int?,degree: String?,profilePic: String?,specialization: String?,visitingTime:String?)
        fun book(doctorsName: String?, doctorsId: Int?,degree: String?,profilePic: String?,specialization: String?,visitingTime:String?)
    }
    companion object {
        var mClickListener: ItemClickListener? = null
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoctorViewHolder {
        mClickListener =listener
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_doctors_adapter, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctorsList?.get(position)
        holder.tvName.text = doctor?.name.toString()
        holder.tvSpecialization.text = doctor?.specialization
        holder.tvDegree.text = doctor?.degree
        holder.tvVisitingTime.text = doctor?.visitingTime

        if (!doctor?.profilePic.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(Urls.IMAGE_BASE+doctor?.profilePic)
                .placeholder(R.drawable.profile_pic)
                .into(holder.ivProfilePic)
        } else {
            holder.ivProfilePic.setImageResource(R.drawable.profile_pic)
        }

        holder.viewProfile.setOnClickListener(View.OnClickListener {
            mClickListener?.viewProfile(
                doctor?.name,doctor?.doctorId,doctor?.degree,doctor?.profilePic,doctor?.specialization,doctor?.visitingTime
            )
        })

        holder.tvBookAppointment.setOnClickListener(View.OnClickListener {
            mClickListener?.book(
                doctor?.name,doctor?.doctorId,doctor?.degree,doctor?.profilePic,doctor?.specialization,doctor?.visitingTime
            )
        })
    }

    override fun getItemCount(): Int {
        return doctorsList?.size ?: 0
    }

    class DoctorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProfilePic: ImageView = view.findViewById(R.id.profilePic)
        val tvName: TextView = view.findViewById(R.id.doctorName)
        val viewProfile: TextView = view.findViewById(R.id.viewProfile)
        val tvSpecialization: TextView = view.findViewById(R.id.tvDesignation)
        val tvDegree: TextView = view.findViewById(R.id.tvDegree)
        val tvVisitingTime: TextView = view.findViewById(R.id.tvVisitingTime)
        val tvBookAppointment: TextView = view.findViewById(R.id.tvBookAppointment)
    }

}