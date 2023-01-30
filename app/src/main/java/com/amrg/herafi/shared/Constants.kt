package com.amrg.herafi.shared

object Constants {
    object CategoryApiKey {
        const val CARPENTER = "home"
        const val ACCOUNTANT = "accountant"
        const val TEACHER = "teacher"
        const val LAWYER = "lawyer"
        const val ENGINEER = "engineer"
        const val DOCTOR = "doctor"
        const val ELECTRICIAN = "electrician"
        const val DESIGNER = "designer"
        const val CONTRACTOR = "contractor"
        const val ASSISTANCE = "assistance"
        const val ALL_CATEGORIES = "all"
    }
    object NavArgs {
        const val CATEGORY_KEY = "category"
        const val CATEGORY_API_KEY_KEY = "category_api_key"
        const val PROJECT_KEY = "project"
        const val IS_ARABIC_KEY = "is_arabic"
        const val EMAIL_KEY = "email"
        const val FULL_NAME_KEY = "full_name"
        const val DONATION_VALUE_KEY = "donation_value"
        const val PROJECT_ID_KEY = "project_id"
    }

    const val NOTIFICATION_CHANNEL_NAME = "Herafi"
}