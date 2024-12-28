package com.example.welfast.Base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment(),BaseView {
    var myProgressDialog: MyProgressDialog? = null
    private var mActivity: BaseActivity? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            mActivity = context
        }
    }


//    fun fragmentTransaction(fragment: Fragment) {
//        val transaction = requireActivity().supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fgTracker, fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
//    }

     override fun pageTransactionFragment(fragment: Fragment, back: String) {
//        val transaction = requireActivity().supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fgTracker, fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
    }

    fun fragmentToActivityIntent(activity: Activity){
        val intent = Intent(requireContext(), activity::class.java)
        startActivity(intent)
    }



    override fun showLoadingIndicator(b: Boolean) {

        myProgressDialog = MyProgressDialog()
//        myProgressDialog!!.initDialog(mActivity)
//
        myProgressDialog!!.setProgress(b,mActivity)
        /* if (b) {

         } else {
             myProgressDialog!!.dismissProgress()
         }*/
    }

    override fun hideLoadingIndicator() {
        if (myProgressDialog != null) {
            myProgressDialog!!.dismissProgress()
        }
    }

    override fun hideKeyboard() {

        // Only runs if there is a view that is currently focused
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}