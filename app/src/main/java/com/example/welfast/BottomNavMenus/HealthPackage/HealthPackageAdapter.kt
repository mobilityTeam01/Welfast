package com.example.welfast.BottomNavMenus.HealthPackage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.welfast.BottomNavMenus.HealthPackage.Models.TestArray
import com.example.welfast.R

class HealthPackageAdapter (private var healthPackageList: ArrayList<TestArray>?, private val listener : ItemClickListener):
    RecyclerView.Adapter<HealthPackageAdapter.HealthPackageViewHolder>(){

    private var filteredList: ArrayList<TestArray>? = healthPackageList

    open interface ItemClickListener {
        fun itemListClick(packageName: String?, packageId: Int?,price: String?,value: String?,unit:String?)
    }
    companion object {
        var mClickListener: ItemClickListener? = null
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HealthPackageViewHolder {
        mClickListener =listener
        val view = LayoutInflater.from(parent.context).inflate(R.layout.health_package_list_adapter, parent, false)
        return HealthPackageViewHolder(view)
    }

    override fun onBindViewHolder(holder: HealthPackageViewHolder, position: Int) {
        val healthPackage = filteredList?.get(position)

        holder.packageHeading.text = healthPackage?.diagnosticTest
        holder.price.text = healthPackage?.price

        holder.packageHeading.setOnClickListener {
            if (holder.llDetails.visibility==View.GONE){
                holder.llDetails.visibility=View.VISIBLE
            }else{
                holder.llDetails.visibility=View.GONE
            }

        }

        holder.llDetails.setOnClickListener(View.OnClickListener {
            mClickListener?.itemListClick(
                healthPackage?.diagnosticTest,healthPackage?.diagnosticTestId,healthPackage?.price,healthPackage?.value,healthPackage?.unit
            )
        })
    }

    override fun getItemCount(): Int {
        return filteredList!!.size
    }

    class HealthPackageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llDetails: LinearLayout = view.findViewById(R.id.llDetails)

        val packageHeading: TextView = view.findViewById(R.id.packageHeading)
        val price: TextView = view.findViewById(R.id.price)

    }

}