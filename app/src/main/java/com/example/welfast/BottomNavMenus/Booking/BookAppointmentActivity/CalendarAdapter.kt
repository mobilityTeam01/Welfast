package com.example.welfast.BottomNavMenus.Booking.BookAppointmentActivity

import android.content.Context
import android.graphics.Color
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.welfast.R

class CalendarAdapter(
    private val context: Context,
    private val days: List<String>,
    private val currentMonth: Calendar,
    private val today: Calendar
) : BaseAdapter() {

    override fun getCount(): Int = days.size

    override fun getItem(position: Int): Any = days[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.calendar_day_item, parent, false)
        val dayTextView = view.findViewById<TextView>(R.id.tvDay)

        val day = days[position]

        // If day is empty (padding days), make the view invisible
        if (day.isEmpty()) {
            dayTextView.visibility = View.INVISIBLE
            return view
        }

        // Parse the day into an integer
        val dayInt = day.toInt()

        // Create a Calendar instance for the current day in the month
        val date = Calendar.getInstance().apply {
            set(
                currentMonth.get(Calendar.YEAR),
                currentMonth.get(Calendar.MONTH),
                dayInt
            )
        }

        // Compare with today's date
        val isPastDate = date.before(today)

        // Apply styles based on whether the date is in the past or future
        if (isPastDate) {
            dayTextView.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
            dayTextView.isEnabled = false // Disable click events for past dates
        } else {
            dayTextView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
            dayTextView.isEnabled = true
        }

        dayTextView.text = day
        return view
    }
}



