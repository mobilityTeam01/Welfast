package com.example.welfast.Base

import androidx.fragment.app.Fragment


interface BaseView {
    fun showLoadingIndicator(b: Boolean)
    fun hideLoadingIndicator()
    fun hideKeyboard()
    fun pageTransactionFragment(fragment: Fragment, back: String)

}