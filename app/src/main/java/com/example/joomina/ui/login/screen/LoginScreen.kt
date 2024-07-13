package com.example.joomina.ui.login.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.joomina.controller.model.ShareViewModel
import com.example.joomina.data.Account
import com.example.joomina.nav.Screens
import com.example.joomina.theme.YellowMain
import com.example.joomina.theme.poppins
import com.example.joomina.ui.main.ButtonCustom
import com.example.joomina.ui.main.TextFieldCustom
import com.example.tanlam.common.isEmptyString
import com.example.tanlam.common.isValidPhoneNumber


@Composable
fun LoginScreen(
    shareViewModel: ShareViewModel,
    navController: NavController,
    showButtonBar: (Boolean) -> Unit,
    getUserName: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            Column(
                modifier.padding(top = 60.dp, start = 16.dp, bottom = 16.dp, end = 16.dp), verticalArrangement = Arrangement.Top
            ) {
                Text(text = "Welcome Back", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Login to your account to continue shopping",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    ) {
        val test = it

        LoginScreenContent(
            shareViewModel = shareViewModel,
            navController = navController,
            showButtonBar = { showButtonBar(it) },
            showUserName = { getUserName(it) }
        )
    }
}

@Composable
fun LoginScreenContent(
    shareViewModel: ShareViewModel,
    navController: NavController,
    showButtonBar: (Boolean) -> Unit,
    showUserName: (String) -> Unit
) {
    var userName by remember { mutableStateOf("") }
    var passWord by remember { mutableStateOf("") }
    var num by remember { mutableStateOf("") }
    var rememberMeState by remember { mutableStateOf(false) }
    var check by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }
    var showDiaLogForgotPass by remember { mutableStateOf(false) }

    var account by remember { mutableStateOf(Account()) }

    var listAccount by remember {
        mutableStateOf(emptyList<Account>())
    }

    //NOTE
    if(userName != "" && passWord != "") {
       shareViewModel.retrieveData(userName = userName, data = {
           account = it
       })

        if(account.userName == userName && account.passWord == passWord) {
            check = true
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.padding(top = 150.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(64.dp))

            Column {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = userName,
                    onValueChange = {
                        userName = it
                    },
                    label = {
                        Text(text = "Username")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                    maxLines = 1,
                    singleLine = true,
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            Column {
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
                        keyboardType = KeyboardType.Password,
                    ),
                    maxLines = 1,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        }


        item {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        showDiaLogForgotPass = true
                    }) {
                    Text(text = "Forgot password?")
                }
            }
        }

        item {
            Text(
                modifier = Modifier.padding(top = 10.dp),
                text = errorText,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = Color.Red
            )
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if(isEmptyString(userName)) {
                        errorText = "invalid Username"
                    }else if(isEmptyString(passWord)) {
                        errorText = "invalid Password"
                    } else {
                        if (check) {
                            if (userName == "admin") {
                                navController.navigate(Screens.AdminScreen.route)
                            }else {
                                showButtonBar(true)
                                showUserName(userName)
                                navController.navigate("${Screens.MainScreen.route}/$userName")
                            }
                        }else {
                            errorText = "Error username or password"
                        }
                    }
                },
                shape = RoundedCornerShape(8),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp), text = "Sign In", textAlign = TextAlign.Center
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = {
                    navController.navigate(Screens.SignupScreen.route)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Don't have an account?")
                        append(" ")
                        withStyle(
                            style = SpanStyle(color = YellowMain, fontWeight = FontWeight.Bold)
                        ) {
                            append("Sign Up")
                        }
                    },
                    fontFamily = poppins,
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

            }
        }
    }

    if (showDiaLogForgotPass) {
        DialogForgotPassword(
            shareViewModel = shareViewModel,
            onDismissRequest = {
                showDiaLogForgotPass = false
            }
        )
    }
}




@Composable
fun DialogForgotPassword(
    shareViewModel: ShareViewModel,
    onDismissRequest: () -> Unit
) {
    var userName by remember { mutableStateOf("") }
    var account by remember { mutableStateOf(Account()) }
    var clickCheck by remember { mutableStateOf(false) }


    Dialog(onDismissRequest = {
        onDismissRequest()
    }) {
        Card() {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Check password",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                TextFieldCustom(title = "Enter username", onValueChange = {
                    userName = it
                    clickCheck = false
                })

                Spacer(modifier = Modifier.height(10.dp))

                //Check password true or false
                if (clickCheck == true) {
                    if (account.passWord != "") {
                        Text(text = "Your password: ${account.passWord}")
                    } else {
                        Text(text = "Error username", color = Color.Red)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                ButtonCustom(
                    title = "Check password",
                    backGroundColor = Color(78, 132, 62),

                    //Check password
                    onClickButton = {
                        account = Account()
                        shareViewModel.retrieveData(
                            userName,
                            data = {
                                account = it
                            }
                        )
                        clickCheck = true
                    }
                )
            }
        }
    }
}
