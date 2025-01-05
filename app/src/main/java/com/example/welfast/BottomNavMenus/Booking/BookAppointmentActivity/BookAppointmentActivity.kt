package com.example.welfast.BottomNavMenus.Booking.BookAppointmentActivity

import android.app.Dialog
import android.os.Bundle
import android.widget.GridView
import android.widget.ImageButton
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.welfast.R
import com.example.welfast.databinding.ActivityBookAppoinmentActvityBinding
import java.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.Locale

class BookAppointmentActivity : AppCompatActivity() {
    lateinit var binding:ActivityBookAppoinmentActvityBinding

    lateinit var from:String
    lateinit var patient:String
    lateinit var patientName:String
    var patientId:Int=0
    lateinit var age:String
    lateinit var relationship:String
    lateinit var opNumber:String

    lateinit var doctorsName:String
    lateinit var doctorsId:String
    lateinit var specialization:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=DataBindingUtil.setContentView(this,R.layout.activity_book_appoinment_actvity)

        val intent = intent

        from = intent.getStringExtra("from").toString()
        patient = intent.getStringExtra("patient").toString()
        patientName = intent.getStringExtra("patientName").toString()
        patientId = intent.getIntExtra("patientId", 0)
        age = intent.getStringExtra("age").toString()
        relationship = intent.getStringExtra("relationship").toString()
        opNumber = intent.getStringExtra("opNumber").toString()
        doctorsName = intent.getStringExtra("doctorsName").toString()
        doctorsId = intent.getStringExtra("doctorsId").toString()
        specialization = intent.getStringExtra("specialization").toString()

        Log.e("FROM",from)
        Log.e("patientName",patientName)
        setData(from)

        setClicks()
    }

    private fun setClicks() {
        binding.buttonBookAppointment.button.text="Book Appointment"
        binding.ivBackButton.ivBack.setOnClickListener { finish() }
        binding.buttonBookAppointment.button.setOnClickListener { validateData() }
        binding.tvSpecialization.setOnClickListener { selectSpecialization() }
        binding.tvDoctors.setOnClickListener { selectDoctor() }
        binding.tvDate.setOnClickListener { setCalendar() }
        binding.tvTime.setOnClickListener { setTime() }
    }

    private fun selectDoctor() {

    }

    private fun selectSpecialization() {

    }

    private fun setTime() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.time_picker_dialog)

        val hourPicker = dialog.findViewById<NumberPicker>(R.id.hourPicker)
        val minutePicker = dialog.findViewById<NumberPicker>(R.id.minutePicker)
        val ampmPicker = dialog.findViewById<NumberPicker>(R.id.ampmPicker)
        val confirmButton = dialog.findViewById<Button>(R.id.btnConfirmTime)


        // Set up hour picker
        hourPicker.minValue = 1
        hourPicker.maxValue = 12
        hourPicker.wrapSelectorWheel = true

        // Set up minute picker
        minutePicker.minValue = 0
        minutePicker.maxValue = 59
        minutePicker.wrapSelectorWheel = true
        minutePicker.setFormatter { i -> String.format("%02d", i) } // Format minutes with leading zero

        // Set up AM/PM picker
        val ampmValues = arrayOf("AM", "PM")
        ampmPicker.minValue = 0
        ampmPicker.maxValue = 1
        ampmPicker.displayedValues = ampmValues
        ampmPicker.wrapSelectorWheel = true

        // Handle button click
        confirmButton.setOnClickListener {
            val selectedHour = hourPicker.value
            val selectedMinute = String.format("%02d", minutePicker.value)
            val selectedAmPm = ampmValues[ampmPicker.value]

            val selectedTime = "$selectedHour:$selectedMinute $selectedAmPm"
            binding.tvTime.text = selectedTime

            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setCalendar() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.calendar_view)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val tvMonthYear = dialog.findViewById<TextView>(R.id.tvMonthYear)
        val calendarGrid = dialog.findViewById<GridView>(R.id.calendarGrid)
        val btnPrev = dialog.findViewById<ImageButton>(R.id.btnPrev)
        val btnNext = dialog.findViewById<ImageButton>(R.id.btnNext)

        val today = Calendar.getInstance()
        val currentMonth = Calendar.getInstance()

        fun updateCalendar() {
            val monthYear = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentMonth.time)
            tvMonthYear.text = monthYear

            val days = getDaysForMonth(currentMonth)
            val adapter = CalendarAdapter(this, days, currentMonth, today)
            calendarGrid.adapter = adapter

            // Disable the "Previous" button if the current month is the same as today's month
            btnPrev.isEnabled = currentMonth.after(today)
        }

        btnPrev.setOnClickListener {
            currentMonth.add(Calendar.MONTH, -1)
            updateCalendar()
        }

        btnNext.setOnClickListener {
            currentMonth.add(Calendar.MONTH, 1)
            updateCalendar()
        }

        calendarGrid.setOnItemClickListener { _, _, position, _ ->
            val days = getDaysForMonth(currentMonth)
            val selectedDay = days[position]

            if (selectedDay.isNotEmpty()) {
                val selectedDate = Calendar.getInstance()
                selectedDate.set(
                    currentMonth.get(Calendar.YEAR),
                    currentMonth.get(Calendar.MONTH),
                    selectedDay.toInt()
                )

                // Allow selection only for today or future dates
                if (!selectedDate.before(today)) {
                    val formattedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(selectedDate.time)
                    binding.tvDate.text = formattedDate
                    dialog.dismiss()
                }
            }
        }

        updateCalendar()
        dialog.show()
    }

    private fun getDaysForMonth(calendar: Calendar): List<String> {
        val days = mutableListOf<String>()
        val tempCalendar = calendar.clone() as Calendar

        // Set the calendar to the first day of the month
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1)

        // Add empty slots for days before the first day of the month
        val firstDayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK) - 1
        for (i in 0 until firstDayOfWeek) {
            days.add("") // Padding days
        }

        // Add the actual days of the month
        val maxDay = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 1..maxDay) {
            days.add(i.toString())
        }

        return days
    }

    private fun validateData(): Boolean {
        if (binding.etName.text.isEmpty()) {
            binding.etName.error = getString(R.string.cannotBeEmpty)
            scroll(binding.etName)
            return false
        }
        if (binding.etAge.text.isEmpty()) {
            binding.etAge.error = getString(R.string.cannotBeEmpty)
            scroll(binding.etAge)
            return false
        }

        if (binding.etPhone.text.isEmpty()) {
            binding.etPhone.error = getString(R.string.cannotBeEmpty)
            scroll(binding.etPhone)
            return false
        }

        if (binding.etEmail.text.isEmpty()) {
            binding.etEmail.error = getString(R.string.cannotBeEmpty)
            scroll(binding.etEmail)
            return false
        } else if (binding.etEmail.text.isNotEmpty()) {
            if (!isEmailValid(binding.etEmail.text.toString())) {
                binding.etEmail.error = getString(R.string.validEmail)
                scroll(binding.etEmail)
                return false
            }
        }

        // Check if any RadioButton is selected
        val selectedRadioButtonId = binding.rgGender.checkedRadioButtonId

        if (selectedRadioButtonId == -1) {
            // No option is selected
            Toast.makeText(this, "Please select an gender", Toast.LENGTH_SHORT).show()
            return false
        } else {
//            // Option is selected, retrieve the selected RadioButton
//            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
//            val selectedText = selectedRadioButton.text.toString()
//
//            // Show the selected option
//            Toast.makeText(this, "Selected: $selectedText", Toast.LENGTH_SHORT).show()
        }

        if (binding.etOpNum.text.isEmpty()) {
            binding.etOpNum.error = getString(R.string.cannotBeEmpty)
            scroll(binding.etOpNum)
            return false
        }

        if (binding.tvSpecialization.text.isEmpty()) {
            binding.tvSpecialization.error = getString(R.string.cannotBeEmpty)
            scrollTv(binding.tvSpecialization)
            return false
        }

        if (binding.tvDoctors.text.isEmpty()) {
            binding.tvDoctors.error = getString(R.string.cannotBeEmpty)
            scrollTv(binding.tvDoctors)
            return false
        }

        if (binding.tvDate.text.isEmpty()) {
            binding.tvDate.error = getString(R.string.cannotBeEmpty)
            scrollTv(binding.tvDate)
            return false
        }

        if (binding.tvTime.text.isEmpty()) {
            binding.tvTime.error = getString(R.string.cannotBeEmpty)
            scrollTv(binding.tvTime)
            return false
        }


        // All inputs are valid
        return true
    }

    private fun scrollTv(textView: TextView) {
        binding.scrollView.post {
            binding.scrollView.smoothScrollTo(0, textView.top)
        }
        textView.requestFocus()
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return email.matches(emailRegex.toRegex())
    }

    private fun scroll(targetEditText: EditText) {

        binding.scrollView.post {
            binding.scrollView.smoothScrollTo(0, targetEditText.top)
        }
        targetEditText.requestFocus()
    }

    private fun setData(from: String) {
        binding.etName.setText(patientName)
        binding.etAge.setText(age)
    }
}