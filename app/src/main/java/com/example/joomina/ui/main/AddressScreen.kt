package com.example.joomina.ui.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.joomina.controller.model.HistoryViewModel
import com.example.joomina.data.Address
import com.example.joomina.data.history.History
import com.example.joomina.data.history.HistoryOfUser
import com.example.joomina.nav.Screens
import com.example.tanlam.common.isEmptyString
import com.example.tanlam.common.isValidPhoneNumber
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddressScreen(
    paddingValues: PaddingValues,
    userName: String,
    price: Double,
    historyViewModel: HistoryViewModel,
    navController: NavController
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var addressTest by remember { mutableStateOf(Address()) }
    var history by remember { mutableStateOf(History()) }

    var error by remember { mutableStateOf("") }
    var showDiaLogCorrect by remember { mutableStateOf(false) }

    val today = LocalDate.now().toString()

    addressTest = Address(
        fullName = fullName,
        email = email,
        address = address,
        phone = phone
    )


    val listHistory = mutableListOf<HistoryOfUser>()
    historyViewModel.retrieveData(userName, data = {
        history = it
    })

    for (i in history.listOfHistory) {
        listHistory.add(i)
    }

    Scaffold(
        modifier = Modifier.padding(top = 40.dp),
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Add Address",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TextFieldCustom(title = "Full Name", isNumber = false) {
                    fullName = it
                }

                TextFieldCustom(title = "Email", isNumber = false) {
                    email = it
                }

                TextFieldCustom(title = "address", isNumber = false) {
                    address = it
                }

                TextFieldCustom(title = "Phone", isNumber = true) {
                    phone = it
                }

                Text(text = error, color = Color.Red)

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Button(onClick = {
                        if (isEmptyString(fullName) || isEmptyString(phone) || isEmptyString(email) || isEmptyString(address)
                        ) {
                            error = "Something is empty"
                        } else if (!isValidPhoneNumber(phone)) {
                            error = "Error phone number"
                        } else {
                            val historyTest = HistoryOfUser(
                                date = today,
                                price = price,
                                address = addressTest
                            )
                            listHistory.add(historyTest)
                            val test = History(userName = userName, listHistory)
                            historyViewModel.addHistory(test)

                            showDiaLogCorrect = true
                        }
                    }) {
                        Text(text = "Add")
                    }
                }
            }
        }
    }

    if(showDiaLogCorrect) {
        CorrectCheckoutDialog {
            showDiaLogCorrect = false
            navController.navigate("${Screens.MainScreen.route}/$userName")
        }
    }
}

@Composable
fun TextFieldCustom(
    title: String,
    isNumber: Boolean,
    onValueChange: (String) -> Unit
) {
    var value by remember { mutableStateOf("") }
    var option by remember { mutableStateOf(KeyboardType.Text) }

    if (isNumber) {
        option = KeyboardType.Number
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = {
            value = it
            onValueChange(value)
        },
        label = {
            Text(text = title)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = option,
        ),
        maxLines = 1,
        singleLine = true,
    )
}

