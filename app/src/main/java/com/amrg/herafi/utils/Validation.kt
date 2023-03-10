package com.amrg.herafi.utils

import android.util.Patterns
import androidx.core.text.isDigitsOnly
import com.amrg.herafi.R

object Validation {
    private const val PHONE_NUMBER_REGEX = "^[+]?[0-9]{10,13}$"

    fun validateEmail(email: String): Int? {
        if (email.isEmpty()) {
            return R.string.cant_be_empty
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return R.string.invalid_email
        }
        return null
    }

    fun validateLoginPassword(password: String): Int? {
        if (password.isEmpty()) {
            return R.string.cant_be_empty
        }
        return null
    }

    fun validateConfirmPassword(password: String): Int? {
        if (password.isEmpty()) {
            return R.string.cant_be_empty
        }
        return null
    }

    fun validateFullName(fullName: String): Int? {
        if (fullName.isEmpty()) {
            return R.string.cant_be_empty
        }
        if(fullName.isDigitsOnly()) {
            return R.string.name_must_contain_chars
        }
        return null
    }

    fun validateRegisterPassword(password: String): Int? {
        if (password.isEmpty()) {
            return R.string.cant_be_empty
        }
        if (password.length < 8) {
            return R.string.password_too_short
        }
        if (password.length > 35) {
            return R.string.password_so_long
        }
        return null
    }

    fun validatePhoneNumber(phoneNumber: String, countryCode: String): Int? {
        if (phoneNumber.isEmpty()) {
            return R.string.cant_be_empty
        }
        if (!PHONE_NUMBER_REGEX.toRegex().matches("+$countryCode$phoneNumber")) {
            return R.string.invalid_phone_number
        }
        return null
    }

}