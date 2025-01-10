package com.example.welfast.BottomNavMenus.Booking.BookAppointmentActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.welfast.BottomNavMenus.Booking.BookAppointmentActivity.Model.SpecializationData
import com.example.welfast.R
import java.util.Locale

class SpecializationAdapter  (private var specializationList: ArrayList<SpecializationData>?, private val listener : ItemClickListener):
    RecyclerView.Adapter<SpecializationAdapter.SpecializationViewHolder>(),
    Filterable {

    private var filteredList: ArrayList<SpecializationData>? = specializationList

    open interface ItemClickListener {
        fun itemListClick(
            specializationName: String?,
            specializationId: Int?
        )
    }

    companion object {
        var mClickListener: ItemClickListener? = null
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SpecializationViewHolder {
        mClickListener = listener
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.booking_specialization_list_adapter, parent, false)
        return SpecializationViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpecializationViewHolder, position: Int) {
        val specialization = filteredList?.get(position)

        holder.specializationNameBooking.text = specialization?.specialization


        holder.specializationNameBooking.setOnClickListener(View.OnClickListener {
            mClickListener?.itemListClick(
                specialization?.specialization,
                specialization?.id
            )
        })
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredList = if (charString.isEmpty()) {
                    specializationList
                } else {
                    val filtered = ArrayList<SpecializationData>()
                    for (specialization in specializationList!!) {
                        if (specialization.specialization?.toLowerCase(Locale.ROOT)
                                ?.contains(charString.toLowerCase(Locale.ROOT)) == true
                        ) {
                            filtered.add(specialization)
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
                    ArrayList()
                } else {
                    results.values as ArrayList<SpecializationData>?
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredList!!.size
    }

    class SpecializationViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val specializationNameBooking: TextView = view.findViewById(R.id.specializationNameBooking)

    }

}