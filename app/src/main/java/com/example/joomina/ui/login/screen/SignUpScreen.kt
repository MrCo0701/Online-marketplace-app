package com.example.joomina.ui.login.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.joomina.controller.model.ShareViewModel
import com.example.joomina.data.Account
import com.example.joomina.nav.Screens
import com.example.joomina.theme.YellowMain
import com.example.joomina.theme.poppins
import com.example.tanlam.common.isEmptyString
import com.example.tanlam.common.isValidEmail

@Composable
fun SignupScreen(navController: NavController, shareViewModel: ShareViewModel) {
    Scaffold(
        topBar = {
            Column(Modifier.padding(top = 60.dp, start = 16.dp, bottom = 16.dp, end = 16.dp), verticalArrangement = Arrangement.Top) {
                Text(text = "Getting Started", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Create an account to continue with your shopping",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }
        },
        modifier = Modifier.padding(top = 30.dp)
    ) {
        val test = it
        SignUpContent(navController = navController, shareViewModel)
    }
}

@Composable
fun SignUpContent(
    navController: NavController,
    shareViewModel: ShareViewModel,
    modifier: Modifier = Modifier
) {

    var userName by remember { mutableStateOf("") }
    var passWord by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var i by remember { mutableStateOf(0) }

    var listAccount by remember {
        mutableStateOf(emptyList<Account>())
    }

    var errorText by remember { mutableStateOf("") }
    var checkMail by remember { mutableStateOf(true) }
    var checkUserName by remember { mutableStateOf(true) }

    //NOTE
    shareViewModel.getAllAccount {
        listAccount = it
    }

    listAccount.forEach { account ->
        if(account.id > i) {
            i = account.id
        }

        if(email == account.email) {
            checkMail = false
        }else {
            checkMail = true
        }

        if(account.userName == userName) {
            checkUserName = false
        }else {
            checkUserName = true
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = modifier.padding(top = 60.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(64.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = userName,
                onValueChange = {
                    userName = it
                },
                label = {
                    Text(text = "User name")
                },
                keyboardOptions = KeyboardOptions(
                    autoCorrect = true,
                    keyboardType = KeyboardType.Email,
                ),
                maxLines = 1,
                singleLine = true,
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = {
                    email = it
                },
                label = {
                    Text(text = "Email")
                },
                keyboardOptions = KeyboardOptions(
                    autoCorrect = true,
                    keyboardType = KeyboardType.Email,
                ),
                maxLines = 1,
                singleLine = true,
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = passWord,
                onValueChange = {
                    passWord = it
                },
                label = {
                    Text(text = "Password")
                },
                keyboardOptions = KeyboardOptions(
                    autoCorrect = true,
                    keyboardType = KeyboardType.Password,
                ),
                maxLines = 1,
                singleLine = true,
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = errorText,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Red
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (isEmptyString(userName)) {
                        errorText = "invalid username"
                    } else if (isEmptyString(passWord)) {
                        errorText = "invalid password"
                    } else if (!isValidEmail(email)) {
                        errorText = "error email"
                    } else {
                        if(checkUserName == false) {
                            errorText = "username already exists"
                        }else if(checkMail == false){
                            errorText = "email already exists"
                        }else {
                            val account = Account(++i, userName, passWord, email)

                            shareViewModel.addAccount(account)
                            navController.navigate(Screens.LoginScreen.route)
                        }
                    }
                },
                shape = RoundedCornerShape(8)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp), text = "Sign Up", textAlign = TextAlign.Center
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = {
                    navController.navigate(Screens.LoginScreen.route)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Already have an account?")
                        append(" ")
                        withStyle(
                            style = SpanStyle(color = YellowMain, fontWeight = FontWeight.Bold)
                        ) {
                            append("Sign In")
                        }
                    },
                    fontFamily = poppins,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}