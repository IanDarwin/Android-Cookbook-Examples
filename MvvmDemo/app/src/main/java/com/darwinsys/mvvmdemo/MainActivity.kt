package com.darwinsys.mvvmdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.darwinsys.mvvmdemo.databinding.ActivityMainBinding

/**
 * Main activity of the registration demo app.
 * The View part of the MVVM.
 * Presents a screen with half a dozen form fields.
 * These are set up in layout/res/activity_main.xml,
 * and upon submit, are validated by code in the
 * ViewModel which communicates status back to us.
 */
class MainActivity : AppCompatActivity() {

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("flow", "onCreate")

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        observeViewModel()
    }

    private fun observeViewModel() {
        Log.d("flow", "observing view model")
        viewModel.resultMessage.observe(this, {msg-> run {
				// XXX Use an AlertDialog, after combining messages
				// into a single string in the ViewModel.
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.resultStatus.observe(this, {
            isSuccess ->
                run {
                    if (isSuccess == true) {
                        Log.d("flow", "Status: Success");
                        startActivity(Intent(this, ThanksActivity::class.java));
                        finish()
                    } else {
                        Log.d("flow", "Status: Failure");
                    }
                }
        })
    }
}
