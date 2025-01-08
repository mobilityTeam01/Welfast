package com.example.welfast.BottomNavMenus.Booking.BookAppointmentActivity

import android.app.Dialog
import android.os.Bundle
import android.widget.GridView
import android.widget.ImageButton
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.databinding.DataBindingUtil
import com.example.welfast.R
import com.example.welfast.databinding.ActivityBookAppoinmentActvityBinding
import java.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import com.example.welfast.Base.BaseActivity
import com.example.welfast.Base.Constance
import com.example.welfast.Base.PreferenceHelper
import com.example.welfast.Base.Retrofit.ApiService
import com.example.welfast.BottomNavMenus.Home.HomeFragment
import com.example.welfast.Dashboard.DashboardActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class BookAppointmentActivity : BaseActivity() {
    lateinit var binding:ActivityBookAppoinmentActvityBinding

    lateinit var from:String
    lateinit var patient:String
    lateinit var patientName:String
    lateinit var patientId:String
    lateinit var age:String
    lateinit var relationship:String
    lateinit var opNumber:String
    lateinit var gender:String

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
        patientId = intent.getStringExtra("patientId").toString()
        age = intent.getStringExtra("age").toString()
        relationship = intent.getStringExtra("relationship").toString()
        opNumber = intent.getStringExtra("opNumber").toString()
        gender = intent.getStringExtra("gender").toString()
        doctorsName = intent.getStringExtra("doctorsName").toString()
        doctorsId = intent.getStringExtra("doctorsId").toString()
        specialization = intent.getStringExtra("specialization").toString()


        setData(from,patient)

        setClicks()
        initializeTextWatchers()
    }

    private fun initializeTextWatchers() {
        addTextChangedListener(binding.etName   )
        addTextChangedListener(binding.etAge    )
        addTextChangedListenerTv(binding.tvSpecialization  )
        addTextChangedListenerTv(binding.tvDoctors)
        addTextChangedListenerTv(binding.tvDate  )
        addTextChangedListenerTv(binding.tvTime  )
    }

    private fun setClicks() {
        binding.buttonBookAppointment.button.text="Book Appointment"
        binding.ivBackButton.ivBack.setOnClickListener { finish() }
        binding.buttonBookAppointment.button.setOnClickListener {
            if (validateData()){ callBookingApi() }
        }

        if (patient == "old"){
            disableAndStyleView(binding.tvSpecialization)
            disableAndStyleView(binding.tvDoctors)
        }else{
            binding.tvSpecialization.setOnClickListener { selectSpecialization() }
            binding.tvDoctors.setOnClickListener { selectDoctor() }
        }

        binding.tvDate.setOnClickListener { setCalendar() }
        binding.tvTime.setOnClickListener { setTime() }
    }

    private fun callBookingApi(){
        showLoadingIndicator(false)

        val params = HashMap<String?, String?>()
        params["patientId"] = patientId
        params["doctorId"] = doctorsId
        params["AppointmentDateTime"] = convertToTimestamp(binding.tvDate.text.toString(),binding.tvTime.text.toString()).toString()

        val authToken = "Bearer"+PreferenceHelper.read(Constance.TOKEN)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.invoke().bookAppointment(authToken,params)
                withContext(Dispatchers.Main) {
                    if (response.status == true) {
                        hideLoadingIndicator()
                        Toast.makeText(this@BookAppointmentActivity, response.message, Toast.LENGTH_SHORT).show()
                        confirmationPopup()
                    } else {
                        hideLoadingIndicator()
                        Toast.makeText(this@BookAppointmentActivity, response.message, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoadingIndicator()
                    Toast.makeText(this@BookAppointmentActivity, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun confirmationPopup() {
        Log.e("confirmationPopup","confirmationPopup")
        val dialog = Dialog(this@BookAppointmentActivity)
        dialog.setContentView(R.layout.appoinment_success_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val ivDone = dialog.findViewById<ImageView>(R.id.ivDone)
        val tvSuccess = dialog.findViewById<TextView>(R.id.tvSuccess)

        ivDone.setOnClickListener {

            dialog.dismiss()
            finish()
            intentActivity(DashboardActivity())
        }
        dialog.show()
    }

    private fun selectDoctor() {

    }

    private fun selectSpecialization() {

    }

    private fun setTime() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.time_picker_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

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
        if (patient=="new"){
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

            if (binding.etEmail.text.isNotEmpty()) {
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
        }

        if (binding.tvSpecialization.text.isEmpty()) {
            binding.tvSpecialization.error = getString(R.string.cannotBeEmpty)
            scroll(binding.tvSpecialization)
            return false
        }

        if (binding.tvDoctors.text.isEmpty()) {
            binding.tvDoctors.error = getString(R.string.cannotBeEmpty)
            scroll(binding.tvDoctors)
            return false
        }

        if (binding.tvDate.text.isEmpty()) {
            binding.tvDate.error = getString(R.string.cannotBeEmpty)
            scroll(binding.tvDate)
            return false
        }

        if (binding.tvTime.text.isEmpty()) {
            binding.tvTime.error = getString(R.string.cannotBeEmpty)
            scroll(binding.tvTime)
            return false
        }
        return true
    }

    private fun scroll(view: View) {

        binding.scrollView.post {
            binding.scrollView.smoothScrollTo(0, view.top)
        }
        view.requestFocus()
    }

    private fun setData(from: String, patient: String) {

        if (patient=="old"){
            binding.tvSpecialization.text = specialization
            binding.tvDoctors.text = doctorsName

            disableAndStyleView(binding.tvSpecialization)
            disableAndStyleView(binding.tvDoctors)
            disableAndStyleView(binding.etName)
            disableAndStyleView(binding.etAge)
            disableAndStyleView(binding.etOpNum)
            disableAndStyleView(binding.etPhone)
            disableAndStyleView(binding.etEmail)

            binding.etName.setText(patientName)
            binding.etAge.setText(age)
            binding.etOpNum.setText(opNumber)
            binding.rbMale.isClickable = false
            binding.rbFeMale.isClickable = false
        }

        //common
        if (!PreferenceHelper.read(Constance.CONTACT_NUMBER).equals("")){
            binding.etPhone.setText(PreferenceHelper.read(Constance.CONTACT_NUMBER).toString())
            disableAndStyleView(binding.etPhone)
        }

        if (!PreferenceHelper.read(Constance.EMAIL).equals("")){
            binding.etEmail.setText(PreferenceHelper.read(Constance.EMAIL).toString())
            disableAndStyleView(binding.etEmail)
        }

    }

    private fun disableAndStyleView(view: View) {

        view.isEnabled = false
        view.setBackgroundResource(R.drawable.grey_fill)
        when (view) {
            is TextView -> view.setTextColor(getResources().getColor(R.color.black))
            is EditText -> view.setTextColor(getResources().getColor(R.color.black))
        }
    }

}