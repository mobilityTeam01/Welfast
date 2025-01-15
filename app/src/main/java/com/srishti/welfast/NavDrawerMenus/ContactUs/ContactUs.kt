package com.srishti.welfast.NavDrawerMenus.ContactUs

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.srishti.welfast.R
import com.srishti.welfast.databinding.ActivityContactUsBinding


class ContactUs : AppCompatActivity() {
    lateinit var binding:ActivityContactUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding= DataBindingUtil.setContentView(this,R.layout.activity_contact_us)

        setClicks()
    }

    private fun setClicks() {
        binding.ivBackButton.ivBack.setOnClickListener { finish() }

        binding.ivMap.setOnClickListener {intentLocation()}
        binding.llAddress.setOnClickListener { intentLocation() }

        binding.llLandline.setOnClickListener { dialPhoneNumber("0481-257-3278") }
        binding.llLandline2.setOnClickListener { dialPhoneNumber("0481-257-3279") }
        binding.llMobile.setOnClickListener { dialPhoneNumber("7561001000") }

        binding.llEmail.setOnClickListener { sendEmail() }
        binding.llWebsite.setOnClickListener { openWebsite() }
    }

    private fun intentLocation() {
        val url = "https://maps.app.goo.gl/ZRURpBAxmpbZBB6PA"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No application found to open the map.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dialPhoneNumber(phoneNumber:String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }

    private fun sendEmail() {

        val intent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "infowelfast@gmail.com", null
            )
        )
        startActivity(Intent.createChooser(intent, "Choose an Email client :"))
    }


    private fun openWebsite() {

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.welfastclinic.com"))
        startActivity(browserIntent)
    }

}