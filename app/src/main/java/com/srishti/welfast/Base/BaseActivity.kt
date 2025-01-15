package com.srishti.welfast.Base

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.icu.util.Calendar
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.srishti.welfast.R
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


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

    fun activityToFragment(fragment: Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
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

    fun checkKeyboardVisibility(linearLayout: LinearLayout,view: View,textView: TextView) {
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            linearLayout.getWindowVisibleDisplayFrame(r)
            val screenHeight: Int = linearLayout.getRootView().height

            // r.bottom is the position above soft keypad or device button.
            // if keypad is shown, the r.bottom is smaller than that before.
            val keypadHeight = screenHeight - r.bottom
            Log.d(TAG, "keypadHeight = $keypadHeight")
            if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                // keyboard is opened
                textView.visibility = View.VISIBLE
                view.visibility = View.GONE
            } else {
                // keyboard is closed
                textView.visibility = View.GONE
                view.visibility = View.VISIBLE
            }
        })
    }

    fun addTextChangedListener(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (editText.error != null) {
                    editText.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun addTextChangedListenerTv(textView: TextView) {
        textView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (textView.error != null) {
                    textView.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun convertToTimestamp(date: String, time: String): String {
        // Define the input formats for the date and time
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        // Define the desired output format
        val timestampFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        timestampFormat.timeZone = TimeZone.getTimeZone("UTC")

        return try {
            // Parse the date and time strings into Date objects
            val parsedDate = dateFormat.parse(date) ?: throw IllegalArgumentException("Invalid date format")
            val parsedTime = timeFormat.parse(time) ?: throw IllegalArgumentException("Invalid time format")

            // Combine the date and time into a single Calendar instance
            val calendar = Calendar.getInstance()
            calendar.time = parsedDate
            val timeCalendar = Calendar.getInstance()
            timeCalendar.time = parsedTime
            calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
            calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            // Format the combined date and time as a timestamp
            timestampFormat.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
            "Invalid input"
        }
    }
}