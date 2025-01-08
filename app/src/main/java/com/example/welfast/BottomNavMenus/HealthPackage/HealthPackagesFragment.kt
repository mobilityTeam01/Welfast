package com.example.welfast.BottomNavMenus.HealthPackage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.welfast.Base.BaseFragment
import com.example.welfast.Base.Constance
import com.example.welfast.Base.PreferenceHelper
import com.example.welfast.Base.Retrofit.ApiService
import com.example.welfast.BottomNavMenus.Doctors.ViewProfile.ViewProfileActivity
import com.example.welfast.BottomNavMenus.HealthPackage.Models.HealthPackageModel
import com.example.welfast.BottomNavMenus.HealthPackage.Models.TestArray
import com.example.welfast.R
import com.example.welfast.databinding.FragmentHealthPackagesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [HealthPackagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HealthPackagesFragment : BaseFragment() {
    lateinit var binding:FragmentHealthPackagesBinding

    var healthPackageList=ArrayList<TestArray>()
    private var healthPackageAdapter: HealthPackageAdapter? = null


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
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_health_packages, container, false)

        val view = binding.root
        callHealthPackageApi()
        return view
    }

    private fun callHealthPackageApi() {

        showLoadingIndicator(false)
        val authToken = "Bearer"+ PreferenceHelper.read(Constance.TOKEN)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiService.invoke().getPackages(authToken)
                withContext(Dispatchers.Main) {
                    if (response.status == true) {
                        hideLoadingIndicator()
                        healthPackageList.addAll(response.diagnosticTest)
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

    private fun setData(response: HealthPackageModel) {
        binding.rvHealthPackage.layoutManager = LinearLayoutManager(requireContext())

        healthPackageAdapter = HealthPackageAdapter(healthPackageList, object : HealthPackageAdapter.ItemClickListener {

            override fun itemListClick(
                packageName: String?,
                packageId: Int?,
                price: String?,
                specialization: String?,
                visitingTime: String?
            ) {
               /* val intent = Intent(requireContext(), ViewProfileActivity()::class.java)
                intent.putExtra("packageName", packageName)
                intent.putExtra("packageId", packageId)
                startActivity(intent)*/
            }
        })

        binding.rvHealthPackage.adapter = healthPackageAdapter

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HealthPackagesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HealthPackagesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}