package app.bensalcie.movieapp.ui.single_movie_details

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import app.bensalcie.movieapp.R
import com.google.android.gms.ads.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_watch.*

class WatchActivity : AppCompatActivity() {
    private lateinit var mInterstitialAd: InterstitialAd
    private lateinit var notificationsDatabase: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationsDatabase = FirebaseDatabase.getInstance().reference.child("MOVIES2021/NOTIFICATIONS/VISITS")

        MobileAds.initialize(this){}

        setContentView(R.layout.activity_watch)
        val movietitle = intent.getStringExtra("name")
        attachPrivacyPolicies(tvmovie_link)
        tvMovieName.text ="3. Search for '$movietitle' and select the season you want."
        mInterstitialAd = InterstitialAd(this).apply {
            adUnitId = "ca-app-pub-5168781139590668/4076760081"
            adListener = (
                    object : AdListener() {
                        override fun onAdLoaded() {

                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        }

                        override fun onAdClosed() {
                        }
                    }
                    )
        }

    }
    private fun attachPrivacyPolicies(tvTermsPrivacy: TextView?) {
        tvTermsPrivacy!!.apply {
            movementMethod = LinkMovementMethod.getInstance()
            val googleUrl = "https://payhip.com/b/QRz8"
            val microsoftUrl = "https://payhip.com/b/QRz8"


            val google = "1. Download "
            val microsoft = "this  app"

            val message = SpannableString("$google $microsoft").apply {
                setLinkSpan(google, googleUrl)
                setLinkSpan(microsoft, microsoftUrl)
            }

            text = message
        }

    }

    private fun SpannableString.setLinkSpan(text: String, url: String) {
        val textIndex = this.indexOf(text)
        setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(url) }
                        .also { startActivity(it) }
                }
            },
            textIndex,
            textIndex + text.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    override fun onStart() {
        super.onStart()
        val systemtime = System.currentTimeMillis().toString()
        notificationsDatabase.child(systemtime).setValue(1)
    }
}