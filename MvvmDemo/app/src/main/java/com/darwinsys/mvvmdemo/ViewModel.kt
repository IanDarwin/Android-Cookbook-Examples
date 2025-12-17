package com.darwinsys.mvvmdemo

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * The ViewModel code for the registration demo.
 * Gets data from the view, validates it,
 * broadcasts status back to the main activity
 * which will, on success, display the next step.
 */
class RegisterViewModel : ViewModel() {

    // Data that the MainActivity (the View) binds to
	// The binding is bidirectional.
    val firstName = MutableLiveData<String>()
    var lastName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val country = MutableLiveData<String>()
    val acceptTerms = MutableLiveData<Boolean>()

    // LiveData for communicating results from ViewModel to View
    val resultStatus = MutableLiveData<Boolean?>()
    val resultMessage = MutableLiveData<String>()

    fun onButtonClick() {
        var userFirstName = firstName.value
        var userLastName = lastName.value
        val userEmail = email.value
        val userWantedLoginName = userName.value
        val userPassword = password.value
        val userCountry = country.value;
        val userAcceptTerms = acceptTerms.value;

        Log.d("flow", "onButtonClick, for $userFirstName $userLastName <$userEmail>.")

        // Simple validation logic
        if (userFirstName.isNullOrEmpty()) {
            resultMessage.value = "First name cannot be empty."
            resultStatus.value = false
        }
        if (userLastName.isNullOrEmpty()) {
            resultMessage.value = "Last name cannot be empty."
            resultStatus.value = false
        }
        if (userEmail.isNullOrEmpty() || ! userEmail.contains("@")) {
            resultMessage.value = "Email missing or invalid"
            resultStatus.value = false
        }
        if (userWantedLoginName.isNullOrEmpty()) {
            resultMessage.value = "Chosen login name cannot be empty."
            resultStatus.value = false
        }
        if (userPassword.isNullOrEmpty() || userPassword.length < 6) {
            resultMessage.value = "Password must be at least 6 characters."
            resultStatus.value = false
        }
        if (userCountry.isNullOrEmpty() || userCountry.length < 2) {
            resultMessage.value = "Country can be name or 2-letter ISO code."
            resultStatus.value = false
        }
        if ((userAcceptTerms == null) || (!userAcceptTerms)){
            resultMessage.value = "Must accept terms in order to register."
            resultStatus.value = false;
        }

        // Simulate a successful login (In a real app, we'd do much more work here)
        if (resultStatus.value == null) { // No errors found
            resultMessage.value = "Registration Succeeded."
            resultStatus.value = true // Notify success
        }
    }
}
