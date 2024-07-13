package com.example.joomina.data.history

import com.example.joomina.data.Address
import java.util.Date

data class History(
    val userName: String = "",
    val listOfHistory:List<HistoryOfUser> = listOf(),
)
