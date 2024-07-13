package com.example.tanlam.common

fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"
    return email.matches(emailRegex.toRegex())
}

fun isEmptyString(input: String): Boolean {
    val regex = Regex("""^\s*$""")
    return regex.matches(input)
}

fun isValidPhoneNumber(phone: String): Boolean {
    val regex = "^(\\+84|0)\\d{9,10}$".toRegex()
    return phone.matches(regex)
}