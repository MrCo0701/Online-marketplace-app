package com.example.joomina.data

data class Comment(
    val account: Account = Account(),
    val value: String = "",
    val id: Int = 0
)
