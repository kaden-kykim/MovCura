package ca.ciccc.wmad.kaden.movcura.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ca.ciccc.wmad.kaden.movcura.R
import ca.ciccc.wmad.kaden.movcura.databinding.ActivityIntroBinding
import ca.ciccc.wmad.kaden.movcura.global.TMDbConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class IntroActivity : AppCompatActivity() {

    private lateinit var layoutIntroCover: FrameLayout
    private lateinit var imageViewIntroLogo: ImageView

    private var job = Job()

    private var checkNext: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val uiScope = CoroutineScope(job + Dispatchers.Main)

        val binding = DataBindingUtil
            .setContentView<ActivityIntroBinding>(this, R.layout.activity_intro)

        layoutIntroCover = binding.layoutIntroCover
        imageViewIntroLogo = binding.imageViewIntroLogo

        val handler = Handler()

        uiScope.launch {
            TMDbConfiguration.setConfData {
                if (it) {
                    startMainActivity()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "There is no response from the Server.\n MovCura will be exited",
                        Toast.LENGTH_LONG
                    ).show()
                    exitProcess(-1)
                }
            }
        }

        runOnUiThread {
            layoutIntroCover.startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.fade_out_1s)
            )

            handler.postDelayed({
                layoutIntroCover.visibility = View.GONE
                imageViewIntroLogo.setImageResource(R.drawable.logo_1)
            }, 1000)

            handler.postDelayed({
                imageViewIntroLogo.setImageResource(R.drawable.logo)
            }, 1500)
        }

        Handler().postDelayed({
            startMainActivity()
        }, 2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun startMainActivity() {
        if (checkNext) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            checkNext = true
        }
    }
}
