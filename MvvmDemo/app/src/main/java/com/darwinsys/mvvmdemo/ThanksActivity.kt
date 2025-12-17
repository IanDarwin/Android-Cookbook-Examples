package com.darwinsys.mvvmdemo

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.darwinsys.mvvmdemo.databinding.ActivityThanksBinding

/**
 * Activity for the next step after registration.
 * Utterly toy example; just shows a Thanks For Registering text.
 */
class ThanksActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_thanks)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_thanks)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
