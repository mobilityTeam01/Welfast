package com.example.welfast.Base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.welfast.R


abstract class BaseActivity : AppCompatActivity(), BaseView {

    var myProgressDialog: MyProgressDialog? = null

    override fun hideKeyboard() {

        // Only runs if there is a view that is currently focused
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }


    override fun showLoadingIndicator(b: Boolean) {
        myProgressDialog = MyProgressDialog()
        Log.e("Indicator", "Loading")
//        myProgressDialog!!.initDialog(this)

        if (b) {
            myProgressDialog!!.setProgress(b, this)
        } else {
            myProgressDialog!!.setProgress(b, this)
        }
    }

    override fun hideLoadingIndicator() {
        if (myProgressDialog != null) {
            myProgressDialog!!.dismissProgress()
        }
    }


    fun intentActivity(activity: Activity) {

        val intent1 = Intent(this, activity::class.java)
        startActivity(intent1)
    }

    override fun pageTransactionFragment(fragment: Fragment, back: String) {

        val transaction = supportFragmentManager.beginTransaction()
        //transaction.replace(R.id.fgTracker, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return email.matches(emailRegex.toRegex())
    }

}