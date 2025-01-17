package com.srishti.welfast.BottomNavMenus.Doctors

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.srishti.welfast.Base.Retrofit.Urls
import com.srishti.welfast.BottomNavMenus.Doctors.DoctorsListModel.DoctorsListData
import com.srishti.welfast.R
import java.util.Locale

class DoctorsListAdapter(private var doctorsList: ArrayList<DoctorsListData>?, private val listener : ItemClickListener):
    RecyclerView.Adapter<DoctorsListAdapter.DoctorViewHolder>() ,
    Filterable {

    private var filteredList: ArrayList<DoctorsListData>? = doctorsList

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
        val doctor = filteredList?.get(position)

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
        return filteredList!!.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                // Normalize the search query
                val charString = constraint?.toString()
                    ?.toLowerCase(Locale.ROOT)
                    ?.replace(".", "")
                    ?.replace(" ", "") // Remove spaces
                    ?.trim() ?: ""

                val results = FilterResults()

                filteredList = if (charString.isEmpty()) {
                    doctorsList // Original list when search query is empty
                } else {
                    val filtered = ArrayList<DoctorsListData>()
                    for (docName in doctorsList!!) {
                        // Normalize the doctor's name
                        val normalizedDoctorName = docName.name
                            ?.toLowerCase(Locale.ROOT)
                            ?.replace(".", "")
                            ?.replace(" ", "") // Remove spaces
                            ?.trim()

                        // Check if the normalized name contains the normalized search query
                        if (normalizedDoctorName?.contains(charString) == true) {
                            filtered.add(docName)
                        }
                    }
                    filtered
                }

                // Assign the filtered results to FilterResults
                results.values = filteredList
                results.count = filteredList!!.size
                return results // Explicitly returning results here
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = if (results?.values != null) {
                    results.values as ArrayList<DoctorsListData>
                } else {
                    ArrayList() // Return an empty list if results are null
                }

                if (filteredList.isNullOrEmpty()) {
                    Log.e("Filter", "No matching results")
                }

                notifyDataSetChanged()
            }
        }
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