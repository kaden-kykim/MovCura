package ca.ciccc.wmad.kaden.movcura.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import ca.ciccc.wmad.kaden.movcura.R
import ca.ciccc.wmad.kaden.movcura.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var layoutMainDrawer: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil
            .setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        layoutMainDrawer = binding.layoutMainDrawer
        navView = binding.navView

        val navController = this.findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, layoutMainDrawer)
        NavigationUI.setupWithNavController(navView, navController)

        navView.itemIconTintList = null
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI
            .navigateUp(findNavController(R.id.nav_host_fragment), layoutMainDrawer)
    }
}
