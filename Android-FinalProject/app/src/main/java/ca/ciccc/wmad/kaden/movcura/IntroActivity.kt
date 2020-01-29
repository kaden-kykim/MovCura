package ca.ciccc.wmad.kaden.movcura

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val handler = Handler()

        runOnUiThread {
            layout_intro_cover.startAnimation(AnimationUtils
                .loadAnimation(this, R.anim.fade_out_1s))

            handler.postDelayed({
                layout_intro_cover.visibility = View.GONE
                imageView_intro_logo.setImageResource(R.drawable.logo_1)
            }, 1000)

            handler.postDelayed({
                imageView_intro_logo.setImageResource(R.drawable.logo)
            }, 1500)
        }

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)
    }
}
