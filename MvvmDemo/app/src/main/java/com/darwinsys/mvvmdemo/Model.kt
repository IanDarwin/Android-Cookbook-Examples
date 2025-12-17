package com.darwinsys.mvvmdemo

/** Data model for the registration demo */
class Model (
    var firstName: String?,
    var lastName: String?,
    var email: String?,
    var userName: String?,
    var password: String?,
    var country: String?,
    var acceptTerms: Boolean = false
)
