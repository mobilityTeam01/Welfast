package com.srishti.welfast.Base

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.srishti.welfast.R

class MyProgressDialog {

    var dialog: Dialog? = null //obj

    fun initDialog(activity: Activity?) {
        dialog = Dialog(activity!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.layout_loading_screen)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    fun setProgress(cancellable: Boolean,activity: Activity?) {
        initDialog(activity)
        dialog?.setCancelable(cancellable)
        try {
            if (dialog!!.isShowing){

            }
            else{
                dialog?.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun dismissProgress() {
        if (dialog!!.isShowing) {
            try {
                dialog!!.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}