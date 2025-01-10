package com.example.welfast.BottomNavMenus.Booking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.welfast.BottomNavMenus.Booking.Model.DataPatientList
import com.example.welfast.R

class PatientListAdapter(private var patientLists: ArrayList<DataPatientList>?, private val listener : ItemClickListener):
     RecyclerView.Adapter<PatientListAdapter.PatientListViewHolder>(){

     private var patientList:ArrayList<DataPatientList>?=patientLists

     open interface ItemClickListener {
         fun itemListClick(patientName: String?, patientId: String?,age: String?,relationship: String?,opNumber:String?,gender:String?)
     }
     companion object {
         var mClickListener: ItemClickListener? = null
     }

     override fun onCreateViewHolder(
         parent: ViewGroup,
         viewType: Int
     ): PatientListViewHolder {
         mClickListener =listener
         val view = LayoutInflater.from(parent.context).inflate(R.layout.choose_patient_adapter, parent, false)
         return PatientListViewHolder(view)
     }

     override fun onBindViewHolder(holder: PatientListViewHolder, position: Int) {
         val data = patientList?.get(position)

         holder.patientName.text = data?.name
         holder.tvRelationName.text = data?.relationship
         holder.tvOpNumber.text = data?.id.toString()
         holder.tvAgeValue.text = data?.age

         holder.rlPatient.setOnClickListener(View.OnClickListener {
             mClickListener?.itemListClick(
                 data?.name, data?.opid,data?.age,data?.relationship,data?.opid,data?.gender
             )
         })
     }

     override fun getItemCount(): Int {
         return patientList?.size!!
     }

     class PatientListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

         val rlPatient: RelativeLayout = view.findViewById(R.id.rlPatient)

         val patientName: TextView = view.findViewById(R.id.patientName)
         val tvRelationName: TextView = view.findViewById(R.id.tvRelationName)
         val tvOpNumber: TextView = view.findViewById(R.id.tvOpNumber)
         val tvAgeValue: TextView = view.findViewById(R.id.tvAgeValue)
     }

 }