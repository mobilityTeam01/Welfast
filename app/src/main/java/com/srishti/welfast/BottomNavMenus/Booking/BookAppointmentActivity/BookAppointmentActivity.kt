package com.srishti.welfast.BottomNavMenus.Booking.BookAppointmentActivity

import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srishti.welfast.Base.BaseActivity
import com.srishti.welfast.Base.Constance
import com.srishti.welfast.Base.PreferenceHelper
import com.srishti.welfast.Base.Retrofit.ApiService
import com.srishti.welfast.BottomNavMenus.Booking.BookAppointmentActivity.Model.GetDoctorData
import com.srishti.welfast.BottomNavMenus.Booking.BookAppointmentActivity.Model.SpecializationData
import com.srishti.welfast.Dashboard.DashboardActivity
import com.srishti.welfast.R
import com.srishti.welfast.databinding.ActivityBookAppoinmentActvityBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale


class BookAppointmentActivity : BaseActivity() {
    lateinit var binding: ActivityBookAppoinmentActvityBinding

    lateinit var from: String
    lateinit var patient: String
    lateinit var patientName: String
    lateinit var patientId: String
    lateinit var age: String
    lateinit var relationship: String
    lateinit var opNumber: String
    lateinit var gender: String

    lateinit var doctorsName: String
    lateinit var doctorsId: String
    lateinit var specialization: String

    var doctorsList = ArrayList<GetDoctorData>()
    private var doctorsListAdapter: BookDoctorsAdapter? = null

    var specializationList = ArrayList<SpecializationData>()
    private var specializationListAdapter: SpecializationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_appoinment_actvity)

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


        setData(from, patient)

        setClicks()
        initializeTextWatchers()

        // Create an InputFilter to allow only letters
        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isLetter(source[i])) {
                    return@InputFilter ""
                }
            }
            null // Accept the input
        }


        // Set the filter to the EditText
        binding.etName.filters = arrayOf(filter)
    }

    private fun initializeTextWatchers() {
        addTextChangedListener(binding.etName)
        addTextChangedListener(binding.etAge)
        addTextChangedListenerTv(binding.tvSpecialization)
        addTextChangedListenerTv(binding.tvDoctors)
        addTextChangedListenerTv(binding.tvDate)
        addTextChangedListenerTv(binding.tvTime)
    }

    private fun setClicks() {
        binding.buttonBookAppointment.button.text = "Book Appointment"
        binding.ivBackButton.ivBack.setOnClickListener { finish() }
        binding.buttonBookAppointment.button.setOnClickListener {
            if (validateData()) {
                if (patient=="new"){
                    callBookingApiNew()
                }else{
                    callBookingApi()
                }
            }
        }

        binding.tvSpecialization.setOnClickListener { selectSpecialization() }

        binding.tvDoctors.setOnClickListener {
            if (binding.tvSpecialization.text.equals("")) {
                Toast.makeText(
                    this@BookAppointmentActivity,
                    "Please select select Specialization",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                selectDoctor()
            }
        }
        callGetSpecializationApi()


        binding.tvDate.setOnClickListener { setCalendar() }
        binding.tvTime.setOnClickListener { setTime() }
    }

    private fun callGetSpecializationApi() {
        showLoadingIndicator(false)
        val authToken = "Bearer" + PreferenceHelper.read(Constance.TOKEN)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.invoke().getSpecialization(authToken)
                withContext(Dispatchers.Main) {
                    if (response.status == true) {
                        hideLoadingIndicator()
                        specializationList.addAll(response.data)
                    } else {
                        hideLoadingIndicator()
                        Toast.makeText(
                            this@BookAppointmentActivity,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoadingIndicator()
                    Toast.makeText(
                        this@BookAppointmentActivity,
                        "An error occurred. Please try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun callGetDoctorApi(specializationId: Int?) {
        showLoadingIndicator(false)

        val params = HashMap<String?, String?>()
        params["specializationId"] = specializationId.toString()


        val authToken = "Bearer" + PreferenceHelper.read(Constance.TOKEN)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.invoke().getDoctor(authToken, params)
                withContext(Dispatchers.Main) {
                    if (response.status == true) {
                        hideLoadingIndicator()
                        doctorsList.addAll(response.doctors)

                    } else {
                        hideLoadingIndicator()
                        Toast.makeText(
                            this@BookAppointmentActivity,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoadingIndicator()
                    Toast.makeText(
                        this@BookAppointmentActivity,
                        "An error occurred. Please try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun callBookingApi() {
        showLoadingIndicator(false)

        val params = HashMap<String?, String?>()
        params["MRN"] = patientId
        params["doctorId"] = doctorsId
        params["AppointmentDate"] =
            convertToTimestamp(binding.tvDate.text.toString(), binding.tvTime.text.toString())

        val authToken = "Bearer" + PreferenceHelper.read(Constance.TOKEN)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.invoke().bookAppointment(authToken, params)
                withContext(Dispatchers.Main) {
                    if (response.status == true) {
                        hideLoadingIndicator()
                        Toast.makeText(
                            this@BookAppointmentActivity,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        confirmationPopup()
                    } else {
                        hideLoadingIndicator()
                        Toast.makeText(
                            this@BookAppointmentActivity,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoadingIndicator()
                    Toast.makeText(
                        this@BookAppointmentActivity,
                        "An error occurred. Please try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun callBookingApiNew() {
        showLoadingIndicator(false)

        val params = HashMap<String?, String?>()
        params["Name"] = binding.etName.text.toString()
        params["doctorId"] = doctorsId
        params["Gender"] = gender
        params["contactNumber"] = binding.etPhone.text.toString()
        params["AppointmentDate"] =
            convertToTimestamp(binding.tvDate.text.toString(), binding.tvTime.text.toString())

        val authToken = "Bearer" + PreferenceHelper.read(Constance.TOKEN)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.invoke().bookAppointmentNew(authToken, params)
                withContext(Dispatchers.Main) {
                    if (response.status == true) {
                        hideLoadingIndicator()
                        Toast.makeText(
                            this@BookAppointmentActivity,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        confirmationPopup()
                    } else {
                        hideLoadingIndicator()
                        Toast.makeText(
                            this@BookAppointmentActivity,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoadingIndicator()
                    Toast.makeText(
                        this@BookAppointmentActivity,
                        "An error occurred. Please try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun confirmationPopup() {
        Log.e("confirmationPopup", "confirmationPopup")
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
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_doctors_list)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val searchView = dialog.findViewById<SearchView>(R.id.searchViewDoctors)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvDoctors)

        recyclerView.layoutManager = LinearLayoutManager(this)

        doctorsListAdapter =
            BookDoctorsAdapter(doctorsList, object : BookDoctorsAdapter.ItemClickListener {

                override fun itemListClick(
                    doctorsName: String?,
                    doctorId: Int?
                ) {
                    binding.tvDoctors.text = doctorsName
                    doctorsId=doctorId.toString()
                    dialog.dismiss()
                }
            })

        recyclerView.adapter = doctorsListAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                doctorsListAdapter!!.filter.filter(newText)
                return true
            }
        })

        dialog.show()
    }

    private fun selectSpecialization() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_specialization_list)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val searchView = dialog.findViewById<SearchView>(R.id.searchViewSpecialization)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.rvSpecialization)

        recyclerView.layoutManager = LinearLayoutManager(this)

        specializationListAdapter = SpecializationAdapter(
            specializationList,
            object : SpecializationAdapter.ItemClickListener {

                override fun itemListClick(
                    specializationName: String?,
                    specializationId: Int?
                ) {
                    binding.tvSpecialization.text = specializationName
                    binding.tvDoctors.text=""
                    dialog.dismiss()
                    callGetDoctorApi(specializationId)
                }
            })

        recyclerView.adapter = specializationListAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                specializationListAdapter!!.filter.filter(newText)
                return true
            }
        })

        dialog.show()
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
        minutePicker.setFormatter { i ->
            String.format(
                "%02d",
                i
            )
        } // Format minutes with leading zero

        // Set up AM/PM picker
        val ampmValues = arrayOf("AM", "PM")
        ampmPicker.minValue = 0
        ampmPicker.maxValue = 1
        ampmPicker.displayedValues = ampmValues
        ampmPicker.wrapSelectorWheel = true

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
            val monthYear =
                SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(currentMonth.time)
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
                    val formattedDate = SimpleDateFormat(
                        "dd MMMM yyyy",
                        Locale.getDefault()
                    ).format(selectedDate.time)
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
        if (patient == "new") {
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

        if (patient == "new") {
            gender=""
            binding.rgGender.setOnCheckedChangeListener { group, checkedId ->
                Log.e("GENDER", checkedId.toString()) // Log the selected RadioButton ID

                when (checkedId) {
                    R.id.rbMale -> {
                        gender = "male"
                    }
                    R.id.rbFeMale -> {
                        gender = "feMale"
                    }
                }
            }

        }

        if (patient == "old" && from == "doctor") {
            //old patient and doctor selected
            binding.tvSpecialization.text = specialization
            binding.tvDoctors.text = doctorsName

            disableAndStyleView(binding.tvSpecialization)
            disableAndStyleView(binding.tvDoctors)
            disableAndStyleView(binding.etName)
            disableAndStyleView(binding.etAge)
            disableAndStyleView(binding.etOpNum)

            binding.etName.setText(patientName)
            binding.etAge.setText(age)
            binding.etOpNum.setText(opNumber)
            binding.rbMale.isClickable = false
            binding.rbFeMale.isClickable = false

            if (gender == "male" || gender == "Male") {
                binding.rgGender.check(R.id.rbMale)
            } else {
                binding.rgGender.check(R.id.rbFeMale)
            }
        }else if (patient == "new" && from == "doctor") {
            //new patient and doctor selected
            binding.etOpNum.visibility=View.GONE

            binding.tvSpecialization.text = specialization
            binding.tvDoctors.text = doctorsName
            disableAndStyleView(binding.tvSpecialization)
            disableAndStyleView(binding.tvDoctors)

        }else if (patient == "old" && from == "patient") {
            //old patient and doctor not selected
            disableAndStyleView(binding.etName)
            disableAndStyleView(binding.etAge)
            disableAndStyleView(binding.etOpNum)

            binding.etName.setText(patientName)
            binding.etAge.setText(age)
            binding.etOpNum.setText(opNumber)
            binding.rbMale.isClickable = false
            binding.rbFeMale.isClickable = false

            if (gender == "male" || gender == "Male") {
                binding.rgGender.check(R.id.rbMale)
            } else {
                binding.rgGender.check(R.id.rbFeMale)
            }
        }else if (patient == "new" && from == "patient") {
            //new patient and doctor not selected
            binding.etOpNum.visibility=View.GONE

        }

        //common
        if (!PreferenceHelper.read(Constance.CONTACT_NUMBER).equals("")) {
            binding.etPhone.setText(PreferenceHelper.read(Constance.CONTACT_NUMBER).toString())
            disableAndStyleView(binding.etPhone)
        }

        if (!PreferenceHelper.read(Constance.EMAIL).equals("")) {
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