package com.example.joomina.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.joomina.R
import com.example.joomina.controller.model.HistoryViewModel
import com.example.joomina.controller.model.ProductViewModel
import com.example.joomina.controller.model.ShareViewModel
import com.example.joomina.data.Account
import com.example.joomina.data.Product
import com.example.joomina.data.history.History
import com.example.joomina.nav.Screens

@Composable
fun AdminScreen(
    productViewModel: ProductViewModel,
    historyViewModel: HistoryViewModel,
    shareViewModel: ShareViewModel,
    navController: NavController
) {
    var listProducts by remember { mutableStateOf(emptyList<Product>()) }
    var listOrders by remember { mutableStateOf(emptyList<History>()) }
    var listAccounts by remember { mutableStateOf(emptyList<Account>()) }
    var numberOrders = 0

    productViewModel.getAllProduct {
        listProducts = it
    }

    historyViewModel.getAllHistories {
        listOrders = it
    }

    shareViewModel.getAllAccount {
        listAccounts = it
    }

    for(i in listOrders) {
        numberOrders += i.listOfHistory.size
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
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
                        navController.navigate(Screens.LoginScreen.route)
                    },
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
                    contentDescription = null
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                androidx.compose.material3.Text(
                    text = "Dash Board",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(8f)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OptionAdmin(
                title = "Total Product",
                number = listProducts.size.toString(),
                icon = R.drawable.box,
                colorBackGround = Color(254, 239, 173),
                colorIcon = Color(243,202,82),
                modifier = Modifier.clickable {
                    navController.navigate(Screens.addProductScreen.route)
                }
            )

            OptionAdmin(
                title = "Total Order",
                number = numberOrders.toString(),
                icon = R.drawable.shop,
                colorBackGround = Color(122, 186, 120),
                colorIcon = Color(10,104,71),
                modifier = Modifier.clickable {
                    navController.navigate(Screens.TotalOrder.route)
                }
            )

            OptionAdmin(
                title = "Total User",
                number = listProducts.size.toString(),
                icon = R.drawable.usericon,
                colorBackGround  = Color(255, 159, 102),
                colorIcon = Color(255,95,0),
                modifier = Modifier.clickable {
                    navController.navigate(Screens.TotalUserScreen.route)
                }
            )
        }
    }
}

@Composable
fun OptionAdmin(
    title: String,
    number: String,
    icon: Int,
    colorBackGround: Color,
    colorIcon: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                colorBackGround,
                shape = RoundedCornerShape(10.dp),
            )
    ) {
        Row (
            modifier = modifier
                .fillMaxWidth()
                .padding(30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            Icon(
                painter = painterResource(id = icon), contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = colorIcon
            )
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = title,
                    fontSize = 22.sp
                )
                Text(text = number, fontWeight = FontWeight.Bold)
            }
        }
    }
}