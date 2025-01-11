package com.example.welfast.BottomNavMenus.Booking

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welfast.Base.BaseFragment
import com.example.welfast.Base.Constance
import com.example.welfast.Base.PreferenceHelper
import com.example.welfast.Base.Retrofit.ApiService
import com.example.welfast.BottomNavMenus.Booking.BookAppointmentActivity.BookAppointmentActivity
import com.example.welfast.BottomNavMenus.Booking.Model.DataPatientList
import com.example.welfast.BottomNavMenus.Booking.Model.PatientListModel
import com.example.welfast.BottomNavMenus.Home.HomeFragment
import com.example.welfast.Dashboard.DashboardActivity
import com.example.welfast.R
import com.example.welfast.databinding.FragmentBookingsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [BookingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookingsFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding:FragmentBookingsBinding
    var patientList=ArrayList<DataPatientList>()
    private var patientListAdapter:PatientListAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_bookings, container, false)
        val view = binding.root

        getPatientListApi()
        setClicks()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                fragmentToActivityIntent(DashboardActivity())
            }
        })

        return view
    }

    private fun setClicks() {
        binding.ivBackButton.ivBack.setOnClickListener { fragmentToActivityIntent(DashboardActivity()) }

        binding.newPatientButton.setOnClickListener {
            val intent = Intent(requireContext(), BookAppointmentActivity()::class.java)
            intent.putExtra("patient", "new")
            intent.putExtra("from", "patient")
            startActivity(intent)
        }
    }

    private fun getPatientListApi() {

        showLoadingIndicator(false)
        val params = HashMap<String?, String?>()
        params["contactNo"] = PreferenceHelper.read(Constance.CONTACT_NUMBER)

        val authToken = "Bearer"+ PreferenceHelper.read(Constance.TOKEN)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.invoke().getPatientList(authToken,params)
                withContext(Dispatchers.Main) {
                    if (response.status == true) {
                        hideLoadingIndicator()
                        patientList.addAll(response.data)
                        setData(response)
                    } else {
                        hideLoadingIndicator()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoadingIndicator()
                    Toast.makeText(requireContext(), "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setData(response: PatientListModel) {

        binding.rvPatients.layoutManager = LinearLayoutManager(requireContext())

        patientListAdapter = PatientListAdapter(patientList, object : PatientListAdapter.ItemClickListener {

            override fun itemListClick(
                patientName: String?,
                patientId: String?,
                age: String?,
                relationship: String?,
                opNumber: String?,
                gender:String?
            ) {
                val intent = Intent(requireContext(), BookAppointmentActivity()::class.java)
                intent.putExtra("from", "patient")
                intent.putExtra("patient", "old")
                intent.putExtra("patientName", patientName)
                intent.putExtra("patientId", patientId)
                intent.putExtra("age", age)
                intent.putExtra("gender", gender)
                intent.putExtra("relationship", relationship)
                intent.putExtra("opNumber", opNumber)
                startActivity(intent)
            }
        })

        binding.rvPatients.adapter = patientListAdapter

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}