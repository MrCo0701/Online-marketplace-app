package com.example.joomina.ui.main

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.joomina.R
import com.example.joomina.controller.model.CartViewModel
import com.example.joomina.controller.model.HistoryViewModel
import com.example.joomina.controller.model.LikeViewModel
import com.example.joomina.controller.model.ShareViewModel
import com.example.joomina.data.Account
import com.example.joomina.data.Like
import com.example.joomina.data.history.History
import com.example.joomina.nav.Screens
import com.example.joomina.theme.GrayColor
import com.example.joomina.theme.YellowMain

@Composable
fun UserScreen(
    userName: String,
    shareViewModel: ShareViewModel,
    historyViewModel: HistoryViewModel,
    cartViewModel: CartViewModel,
    likeViewModel: LikeViewModel,
    navController: NavController,
    showButtonBar: (Boolean) -> Unit
) {
    Scaffold(
        backgroundColor = Color.White,
        topBar = {
            TopAppBar(
                elevation = 1.dp,
                backgroundColor = Color.White,
                title = {
                    androidx.compose.material.Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "My Profile",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        }
    ) {
        val i = it
        AccountScreenContent(
            userName = userName,
            shareViewModel = shareViewModel,
            historyViewModel = historyViewModel,
            cartViewModel = cartViewModel,
            navController = navController,
            likeViewModel = likeViewModel,
            onClickSignOut = {
                showButtonBar(it)
            }
        )
    }
}

@Composable
private fun AccountScreenContent(
    userName: String,
    shareViewModel: ShareViewModel,
    historyViewModel: HistoryViewModel,
    cartViewModel: CartViewModel,
    navController: NavController,
    likeViewModel: LikeViewModel,
    onClickSignOut: (Boolean) -> Unit,
) {
    // HISTORY
    var history by remember { mutableStateOf(History()) }
    var numberOfOrder by remember { mutableStateOf(0) }
    historyViewModel.retrieveData(userName = userName, data = {
        history = it
    })
    numberOfOrder = history.listOfHistory.size

    //LIKE PRODUCT
    var like by remember { mutableStateOf(Like()) }
    var numberOfLike by remember { mutableStateOf(0) }
    likeViewModel.retrieveData(userName = userName, data = {
        like = it
    })
    numberOfLike = like.listIdProduct.size

    //Change pass
    var isChangePass by remember { mutableStateOf(false) }
    var account by remember { mutableStateOf(Account()) }
    shareViewModel.retrieveData(userName = userName, data = {
        account = it
    })
    if(isChangePass) {
        DialogChangePass(account = account, shareViewModel = shareViewModel, onDismissRequest = {isChangePass = false})
    }

    LazyColumn {
        item {
            UserItem(
                userName = userName,
                shareViewModel = shareViewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(4.dp)
            )
        }

        item {
            Card(
                modifier = Modifier.padding(8.dp),
                border = BorderStroke(0.3.dp, GrayColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "My order",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$numberOfOrder orders",
                            color = Color.Black,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(
//                            imageVector = Icons.Outlined.Delete,
//                            contentDescription = null
//                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.padding(8.dp),
                border = BorderStroke(0.3.dp, GrayColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Like Products",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$numberOfLike products were liked",
                            color = Color.Black,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(
//                            imageVector = Icons.Outlined.Delete,
//                            contentDescription = null
//                        )
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.padding(8.dp),
                border = BorderStroke(0.3.dp, GrayColor),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Setting",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Change your password",
                            color = Color.Black,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp
                        )
                    }
                    IconButton(onClick = { isChangePass = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = null
                        )
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    onClickSignOut(false)
                    navController.navigate(Screens.LoginScreen.route)
                },
                shape = RoundedCornerShape(8)
            ) {
                androidx.compose.material.Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    text = "Sign Out",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun UserItem(
    userName: String,
    shareViewModel: ShareViewModel,
    modifier: Modifier = Modifier,
) {
    var account by remember { mutableStateOf(Account()) }
    shareViewModel.retrieveData(userName = userName, data = {
        account = it
    })

    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = 3.dp
    ) {
        Row {
            if (account.image != "") {
                Image(
                    painter = rememberImagePainter(
                        request = ImageRequest.Builder(LocalContext.current)
                            .data(account.image)
                            .apply {
                                placeholder(R.drawable.ic_placeholder) // Placeholder trong khi tải
                                crossfade(true) // Hiệu ứng chuyển đổi mượt
                            }
                            .build()
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(1f)
                        .clip(CircleShape)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Inside
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(1f)
                        .clip(CircleShape)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Inside
                )
            }
            Spacer(modifier = Modifier.width(5.dp))

            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(5.dp),
                horizontalAlignment = Alignment.Start
            ) {
                androidx.compose.material.Text(
                    text = account.nickName,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(5.dp))
                androidx.compose.material.Text(
                    text = "@$userName",
                    color = Color.Black,
                    fontSize = 16.sp,
                    maxLines = 3,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.align(Alignment.End),
                    onClick = {
                        showDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        backgroundColor = YellowMain
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    androidx.compose.material.Text(
                        modifier = Modifier
                            .padding(3.dp),
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        text = "Edit profile"
                    )
                }
            }
        }
    }

    if (showDialog) {
        DialogCustom(
            account = account,
            shareViewModel = shareViewModel,
            onDismissRequest = {
                showDialog = false
            }
        )
    }
}

@Composable
fun DialogCustom(
    account: Account,
    shareViewModel: ShareViewModel,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    var imgUri by remember { mutableStateOf("") }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val laucher = rememberLauncherForActivityResult( // lay anh tu thu vien
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        val let = uri?.let {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }

    Dialog(onDismissRequest = {
        onDismissRequest()
    }) {
        Card(
            modifier = Modifier
                .height(300.dp)
                .width(300.dp),
            shape = RoundedCornerShape(30.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .height(120.dp)
                        .width(160.dp)
                        .clickable {
                            laucher.launch("image/*")
                        }
                        .drawBehind {
                            val strokeWidth = 3.dp.toPx()
                            val pathEffect =
                                PathEffect.dashPathEffect(
                                    floatArrayOf(4.dp.toPx(), 5.dp.toPx()),
                                    0f
                                )
                            drawRoundRect(
                                color = Color(43, 122, 252),
                                size = size,
                                cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
                                style = Stroke(
                                    width = strokeWidth,
                                    pathEffect = pathEffect,
                                    cap = StrokeCap.Butt,
                                    join = StrokeJoin.Miter
                                )
                            )
                        },
                    shape = RoundedCornerShape(16.dp),
                ) {
                    if (bitmap == null) {
                        if (account.image == "") {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.imageupdate),
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp)
                                )
                                Text(text = "Drop images here")
                            }
                        } else {
                            Image(
                                painter = rememberImagePainter(
                                    request = ImageRequest.Builder(LocalContext.current)
                                        .data(account.image)
                                        .apply {
                                            placeholder(R.drawable.ic_placeholder) // Placeholder trong khi tải
                                            crossfade(true) // Hiệu ứng chuyển đổi mượt
                                        }
                                        .build()
                                ),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    } else {
                        Image(
                            bitmap = bitmap!!.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )

                        shareViewModel.uploadImage(
                            userName = account.userName,
                            bitmap = bitmap!!
                        ) { success, imageUrl ->
                            if (success) {
                                imageUrl.let {
                                    imgUri = it
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                var nicknametest by remember { mutableStateOf(account.nickName) }
                TextFieldCustom(title = "Enter nick name", onValueChange = {
                    nicknametest = it
                })
                val accountTest = Account(
                    account.id,
                    account.userName,
                    account.passWord,
                    account.email,
                    image = imgUri,
                    nickName = nicknametest
                )
                Spacer(modifier = Modifier.height(10.dp))
                ButtonCustom(
                    title = "Change",
                    backGroundColor = YellowMain,
                    onClickButton = {
                        shareViewModel.addAccount(accountTest)
                        onDismissRequest()
                    }
                )
            }
        }
    }
}

@Composable
fun TextFieldCustom(
    title: String,
    onValueChange: (String) -> Unit,
    numberKeyBoard: Boolean ?= null,
    valueIfHave: String? = null,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    var keyboardType by remember { mutableStateOf(KeyboardType.Text) }


    if(numberKeyBoard != null) {
        keyboardType = KeyboardType.Number
    }

    if (valueIfHave != null) {
        text = valueIfHave
    }

    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = {
            text = it
            onValueChange(text)
        },
            label = {
            Text(text = title)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
        ),
        maxLines = 1,
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
    )
}


@Composable
fun ButtonCustom(
    title: String,
    backGroundColor: Color,
    onClickButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Button(
        onClick = {
            onClickButton()
        },
        modifier = modifier,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            backGroundColor,
        )
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun DialogChangePass(
    account: Account,
    shareViewModel: ShareViewModel,
    onDismissRequest: () -> Unit
) {
    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }
    Dialog(onDismissRequest = {
        onDismissRequest()
    }) {
        Card(
            modifier = Modifier
                .height(300.dp)
                .width(300.dp),
            shape = RoundedCornerShape(30.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Change Password", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(30.dp))

                TextFieldCustom(
                    title = "Old password",
                    onValueChange = {
                        oldPass = it
                    }
                )

                TextFieldCustom(
                    title = "New password",
                    onValueChange = {
                        newPass = it
                    }
                )
                Text(text = errorText, color = Color.Red)

                Spacer(modifier = Modifier.height(10.dp))
                ButtonCustom(
                    title = "Change",
                    backGroundColor = YellowMain,
                    onClickButton = { 
                        if(oldPass == account.passWord) {
                            if(newPass == "") {
                                errorText = "You don't fill new pass"
                            }else if(newPass == oldPass) {
                                errorText = "Your pass no change with old pass"
                            }else {
                                val newAccount = Account(
                                    account.id,
                                    account.userName,
                                    newPass,
                                    account.email,
                                    account.image,
                                    account.nickName
                                )

                                shareViewModel.addAccount(newAccount)
                                onDismissRequest()
                            }
                        }else {
                            errorText = "Error old password"
                        }
                    }
                )
            }
        }
    }
}