package com.example.welfast.BottomNavMenus.Home.InnerActivities.MedicalReport.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.welfast.BottomNavMenus.Home.InnerActivities.MedicalReport.Model.Prescription
import com.example.welfast.R

class PrescriptionAdapter (private var prescriptionList: ArrayList<Prescription>?, private val listener : ItemClickListener):
    RecyclerView.Adapter<PrescriptionAdapter.PrescriptionViewHolder>() {

    open interface ItemClickListener {
        fun itemListClick(drug: String?,dosage: String?)
    }
    companion object {
        var mClickListener: ItemClickListener? = null
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PrescriptionViewHolder {
        mClickListener =listener
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_prescription_adapter, parent, false)
        return PrescriptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrescriptionViewHolder, position: Int) {
        val prescription = prescriptionList?.get(position)
        holder.tvDrug.text = prescription?.drug
        holder.tvDosage.text = prescription?.dosage
        //holder.slNo.text = prescription?.

        holder.llPrescription.setOnClickListener(View.OnClickListener {
            mClickListener?.itemListClick(
                prescription?.drug,prescription?.dosage
            )
        })
    }

    override fun getItemCount(): Int {
        return prescriptionList?.size ?: 0
    }

    class PrescriptionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDrug: TextView = view.findViewById(R.id.tvDrug)
        val tvDosage: TextView = view.findViewById(R.id.tvDosage)
        val slNo: TextView = view.findViewById(R.id.slNo)
        val llPrescription: LinearLayout = view.findViewById(R.id.llPrescription)

    }

}