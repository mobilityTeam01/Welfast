package com.example.welfast.BottomNavMenus.Home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welfast.Base.BaseFragment
import com.example.welfast.Base.Constance
import com.example.welfast.Base.PreferenceHelper
import com.example.welfast.Base.Retrofit.ApiService
import com.example.welfast.BottomNavMenus.Doctors.PatientListActivity.PatientListActivity
import com.example.welfast.BottomNavMenus.Home.InnerActivities.MedicalReport.MedicalReportActivity
import com.example.welfast.BottomNavMenus.Home.Model.DoctorsHome
import com.example.welfast.BottomNavMenus.Home.Model.HomeModel
import com.example.welfast.Dashboard.DashboardActivity
import com.example.welfast.R
import com.example.welfast.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentHomeBinding
    var doctorsList=ArrayList<DoctorsHome>()
    private var doctorsListAdapter: DoctorsListHomeAdapter? = null


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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val view = binding.root

        setClicks()
        callHomeApi()
        return view
    }

    private fun callHomeApi() {

        showLoadingIndicator(false)
        val params = HashMap<String?, String?>()
        params["patientId"] = PreferenceHelper.read(Constance.PATIENT_ID)
        val authToken = "Bearer"+ PreferenceHelper.read(Constance.TOKEN)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.invoke().getHomeDetails(authToken,params)
                withContext(Dispatchers.Main) {
                    if (response.status == true) {
                        hideLoadingIndicator()
                        doctorsList.addAll(response.doctors)
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

    private fun setData(response: HomeModel) {
        binding.rvDoctorsList.layoutManager = LinearLayoutManager(requireContext())
        //limit touch (to avoid scrolling)
        binding.rvDoctorsList.setOnTouchListener { _, _ -> true }

        doctorsListAdapter = DoctorsListHomeAdapter(doctorsList, object : DoctorsListHomeAdapter.ItemClickListener {

            override fun itemListClick(
                doctorsName: String?,
                doctorsId: Int?,
                profilePic: String?,
                specialization: String?,
                visitingTime: String?,
                size: Int
            ) {
                val intent = Intent(requireContext(), PatientListActivity()::class.java)
                intent.putExtra("doctorsName", doctorsName)
                intent.putExtra("doctorsId", doctorsId.toString())
                intent.putExtra("profilePic", profilePic)
                intent.putExtra("specialization", specialization)
                intent.putExtra("visitingTime", visitingTime)
                intent.putExtra("from", "doctors")
                startActivity(intent)

            }
        })

        binding.rvDoctorsList.adapter = doctorsListAdapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean { return false }
            override fun onQueryTextChange(newText: String?): Boolean {
                Log.e("FILTER SIZE",doctorsList.size.toString())
                doctorsListAdapter!!.filter.filter(newText)
                return true
            }
        })

        Log.e("PS",response.patientReport?.prescription?.size.toString())
        if (response.patientReport?.prescription?.size==0){

            binding.tvNoData.visibility=View.VISIBLE
            binding.tvSeeDetails.visibility=View.GONE

            binding.tvVisit.visibility=View.GONE
            binding.tvName.visibility=View.GONE
        }else{
            binding.tvVisit.text=getString(R.string.visitingTimeHome)+response.patientReport?.visitDate
            binding.tvName.text=getString(R.string.doctorsNameHome)+response.patientReport?.doctorName
        }

    }

    private fun setClicks() {
        binding.openDrawer.setOnClickListener {
            (activity as? DashboardActivity)?.openDrawer()
        }

        binding.tvSeeDetails.setOnClickListener{
            fragmentToActivityIntent(MedicalReportActivity())
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}