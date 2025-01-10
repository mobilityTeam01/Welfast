package com.example.welfast.BottomNavMenus.Home

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
import com.example.welfast.Base.Retrofit.Urls
import com.example.welfast.BottomNavMenus.Home.Model.DoctorsHome
import com.example.welfast.R
import java.util.Locale

class DoctorsListHomeAdapter(private var doctorsList: ArrayList<DoctorsHome>?, private val listener : ItemClickListener):
    RecyclerView.Adapter<DoctorsListHomeAdapter.DoctorViewHolder>(),
    Filterable {

    private var filteredList: ArrayList<DoctorsHome>? = doctorsList

    open interface ItemClickListener {
        fun itemListClick(
            doctorsName: String?,
            doctorsId: Int?,
            profilePic: String?,
            specialization: String?,
            visitingTime: String?,
            size: Int
        )
    }
    companion object {
        var mClickListener: ItemClickListener? = null
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoctorViewHolder {
        mClickListener =listener
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_page_doctors_list_adapter, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = filteredList?.get(position)

        holder.doctorNameHome.text = doctor?.name.toString()
        holder.tvDepartment.text = doctor?.specialization

        if (!doctor?.profilePic.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(Urls.IMAGE_BASE+doctor?.profilePic)
                .placeholder(R.drawable.profile_pic)
                .into(holder.ivProfilePic)
        } else {
            holder.ivProfilePic.setImageResource(R.drawable.profile_pic)
        }

        holder.tvAppointment.setOnClickListener(View.OnClickListener {
            mClickListener?.itemListClick(
                doctor?.name,doctor?.doctorId,doctor?.profilePic,doctor?.specialization,doctor?.visitingTime,filteredList!!.size
            )
        })
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
                    val filtered = ArrayList<DoctorsHome>()
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
                    results.values as ArrayList<DoctorsHome>
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


    override fun getItemCount(): Int {
        return filteredList!!.size
    }

    class DoctorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivProfilePic: ImageView = view.findViewById(R.id.profilePicHome)

        val doctorNameHome: TextView = view.findViewById(R.id.doctorNameHome)
        val tvDepartment: TextView = view.findViewById(R.id.tvDepartment)
        val tvAppointment: TextView = view.findViewById(R.id.tvAppointment)
    }

}