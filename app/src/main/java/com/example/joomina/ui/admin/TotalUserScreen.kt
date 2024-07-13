package com.example.joomina.ui.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.joomina.R
import com.example.joomina.controller.model.CartViewModel
import com.example.joomina.controller.model.HistoryViewModel
import com.example.joomina.controller.model.LikeViewModel
import com.example.joomina.controller.model.ShareViewModel
import com.example.joomina.data.Account

@Composable
fun TotalUserScreen(
    shareViewModel: ShareViewModel,
    historyViewModel: HistoryViewModel,
    cartViewModel: CartViewModel,
    likeViewModel: LikeViewModel,
    navController: NavController
) {
    var listAccount by remember { mutableStateOf(emptyList<Account>()) }
    shareViewModel.getAllAccount {
        listAccount = it
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(255, 159, 102))
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 30.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .width(40.dp)
                    .shadow(12.dp, shape = RoundedCornerShape(40.dp))
                    .background(color = Color.White)
                    .clickable {
                        navController.popBackStack()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
                    contentDescription = null
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Total User",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(8f)
                .background(
                    Color.White, shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(listAccount.size) { index ->
                if(listAccount[index].userName != "admin") {
                    UserItemCard(
                        listAccount[index],
                        shareViewModel = shareViewModel,
                        historyViewModel = historyViewModel,
                        cartViewModel = cartViewModel,
                        likeViewModel = likeViewModel,
                        getListAccountAfterDelete = {
                            listAccount = it
                        },
                        border = 2,
                        colorBorder = Color(221, 223, 225)
                    )
                }
            }
        }
    }
}

@Composable
fun UserItemCard(
    account: Account,
    shareViewModel: ShareViewModel,
    historyViewModel: HistoryViewModel,
    cartViewModel: CartViewModel,
    likeViewModel: LikeViewModel,
    getListAccountAfterDelete: (List<Account>) -> Unit,
    border: Int,
    colorBorder: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        elevation = 8.dp
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "User ID: ${account.id}",
                    modifier = Modifier
                        .padding(10.dp)
                )

                TextButton(onClick = {
                    shareViewModel.deleteData(account.userName)
                    historyViewModel.deleteData(account.userName)
                    cartViewModel.deleteData(account.userName)
                    likeViewModel.deleteData(account.userName)

                    shareViewModel.getAllAccount {
                        getListAccountAfterDelete(it)
                    }
                }) {
                    Text(text = "Delete", color = Color.Red)
                }
            }

            Box(
                modifier = Modifier
                    .height(border.dp)
                    .background(colorBorder)
                    .fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
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
                            .clip(RoundedCornerShape(10.dp))
                            .size(100.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .size(100.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .height(border.dp)
                    .background(colorBorder)
                    .fillMaxWidth()
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Username",
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(border.dp)
                                .background(colorBorder)
                                .fillMaxHeight()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = account.userName,
                                maxLines = 1,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Box(modifier = Modifier
                        .height(border.dp)
                        .background(colorBorder)
                        .fillMaxWidth())

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Email",
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(border.dp)
                                .background(colorBorder)
                                .fillMaxHeight()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = account.email,
                                maxLines = 2,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Box(modifier = Modifier
                        .height(border.dp)
                        .background(colorBorder)
                        .fillMaxWidth())

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nick Name",
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(border.dp)
                                .background(colorBorder)
                                .fillMaxHeight()
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = account.nickName,
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
