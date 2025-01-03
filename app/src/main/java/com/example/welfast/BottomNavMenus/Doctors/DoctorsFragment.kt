package com.example.welfast.BottomNavMenus.Doctors

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.welfast.Base.BaseFragment
import com.example.welfast.Base.Retrofit.ApiService
import com.example.welfast.BottomNavMenus.Doctors.DoctorsListModel.DoctorsListData
import com.example.welfast.BottomNavMenus.Doctors.ViewProfile.ViewProfileActivity
import com.example.welfast.R
import com.example.welfast.databinding.FragmentDoctorsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [DoctorsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DoctorsFragment : BaseFragment() {

    lateinit var binding: FragmentDoctorsBinding

    var doctorsList=ArrayList<DoctorsListData>()
    private var doctorsListAdapter: DoctorsListAdapter? = null

    private var param1: String? = null
    private var param2: String? = null

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
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_doctors, container, false)
        val view = binding.root
        callGetDoctorsApi()
        return view
    }

    private fun callGetDoctorsApi() {
        showLoadingIndicator(false)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.invoke().getDoctors()
                withContext(Dispatchers.Main) {
                    if (response.status == true) {
                        hideLoadingIndicator()
                        doctorsList.addAll(response.doctorsList)
                        setList()
                    } else {
                        hideLoadingIndicator()
                        Toast.makeText(requireContext(), "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideLoadingIndicator()
                    Log.e("GetDetailsApi", "Error fetching data", e)
                    Toast.makeText(requireContext(), "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setList() {

        binding.rvDoctorsListMain.layoutManager = LinearLayoutManager(requireContext())

        doctorsListAdapter = DoctorsListAdapter(doctorsList, object : DoctorsListAdapter.ItemClickListener {

            override fun itemListClick(
                doctorsName: String?,
                doctorsId: Int?,
                degree: String?,
                profilePic: String?,
                specialization: String?,
                visitingTime: String?
            ) {
                val intent = Intent(requireContext(), ViewProfileActivity()::class.java)
                intent.putExtra("doctorsName", doctorsName)
                intent.putExtra("doctorsId", doctorsId)
                intent.putExtra("degree", degree)
                intent.putExtra("profilePic", profilePic)
                intent.putExtra("specialization", specialization)
                intent.putExtra("visitingTime", visitingTime)
                startActivity(intent)
            }
        })
        binding.rvDoctorsListMain.adapter = doctorsListAdapter
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DoctorsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DoctorsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}