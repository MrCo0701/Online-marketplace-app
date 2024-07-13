package com.example.joomina.data.history

import com.example.joomina.data.Address

data class HistoryOfUser(
    val date: String = "",
    val price: Double = 0.0,
    val address: Address = Address()
)
