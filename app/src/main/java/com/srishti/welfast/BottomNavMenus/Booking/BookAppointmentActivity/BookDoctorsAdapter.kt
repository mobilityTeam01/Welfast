package com.srishti.welfast.BottomNavMenus.Booking.BookAppointmentActivity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.srishti.welfast.BottomNavMenus.Booking.BookAppointmentActivity.Model.GetDoctorData
import com.srishti.welfast.R
import java.util.Locale

class BookDoctorsAdapter (private var doctorsList: ArrayList<GetDoctorData>?, private val listener : ItemClickListener):
    RecyclerView.Adapter<BookDoctorsAdapter.DoctorViewHolder>(),
    Filterable {

    private var filteredList: ArrayList<GetDoctorData>? = doctorsList

    open interface ItemClickListener {
        fun itemListClick(
            doctorsName: String?,
            doctorId: Int?
        )
    }

    companion object {
        var mClickListener: ItemClickListener? = null
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoctorViewHolder {
        mClickListener = listener
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.booking_doctors_list_adapter, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = filteredList?.get(position)

        holder.doctorNameHome.text = doctor?.name.toString()


        holder.doctorNameHome.setOnClickListener(View.OnClickListener {
            mClickListener?.itemListClick(
                doctor?.name,
                doctor?.doctorId
            )
        })
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredList = if (charString.isEmpty()) {
                    doctorsList
                } else {
                    val filtered = ArrayList<GetDoctorData>()
                    for (docName in doctorsList!!) {
                        if (docName.name?.toLowerCase(Locale.ROOT)
                                ?.contains(charString.toLowerCase(Locale.ROOT)) == true
                        ) {
                            filtered.add(docName)
                        }
                    }
                    filtered
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = if (results?.values == null) {
                    Log.e("List", "null")
                    ArrayList()
                } else {
                    Log.e("List", "Collegelist")
                    results.values as ArrayList<GetDoctorData>?
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredList!!.size
    }

    class DoctorViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val doctorNameHome: TextView = view.findViewById(R.id.doctorNameBooking)

    }

}