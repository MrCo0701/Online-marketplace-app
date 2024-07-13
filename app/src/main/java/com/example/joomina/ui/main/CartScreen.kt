package com.example.joomina.ui.main

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.joomina.controller.model.ProductViewModel
import com.example.joomina.controller.model.ShareViewModel
import com.example.joomina.data.Cart
import com.example.joomina.data.Product
import com.example.joomina.data.history.History
import com.example.joomina.data.history.HistoryOfUser
import com.example.joomina.nav.Screens
import java.time.LocalDate

@Composable
fun CartScreen(
    userName: String,
    historyViewModel: HistoryViewModel,
    shareViewModel: ShareViewModel,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    navController: NavController
) {
    // CART
    var cart by remember { mutableStateOf(Cart()) }
    var resetList by remember { mutableStateOf(false) }
    val listProductOfUser = mutableListOf<Product>()
    var productAfterGet by remember { mutableStateOf(Product()) }
    cartViewModel.retrieveData(userName, data = { cart = it }, onError = {})

    // When checkout, we will to reset the cart
    if (resetList) {
        val list = listOf<Int>()
        val cartRemoveList = Cart(userName, list)
        cartViewModel.addCart(cartRemoveList)
    }

    var listAllProduct by remember { mutableStateOf(emptyList<Product>()) }
    productViewModel.getAllProduct {
        listAllProduct = it
    }

    for (id in cart.listProduct) {
        for (i in listAllProduct) {
            if (id == i.id) {
                listProductOfUser.add(i)
            }
        }
    }


    // HISTORY
    var history by remember { mutableStateOf(History()) }
    val listHistoryOfUser = mutableListOf<HistoryOfUser>()
    historyViewModel.retrieveData(userName = userName, data = { history = it })
    for (i in history.listOfHistory) {
        listHistoryOfUser.add(i)
    }

    var productTest by remember { mutableStateOf(Product()) }

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
                        text = "My Cart",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
            )
        },
        modifier = Modifier.padding(top = 35.dp)
    ) {
        CartScreenContent(
            userName = userName,
            listProductOfUser,
            resetProduct = {
                resetList = true
            },
            cartViewModel = cartViewModel,
            onDeleteProduct = {
                productViewModel.retrieveData(it, data = { pro ->
                    productTest = pro
                })

                listProductOfUser.remove(productTest)
                val listIntProducts = mutableListOf<Int>()
                for (i in listProductOfUser) {
                    listIntProducts.add(i.id)
                }

                cartViewModel.addCart(Cart(userName, listIntProducts))
            },
            onUpdateHistory = {
//                listHistoryOfUser.add(it)
//                val historyTest = History(userName, listHistoryOfUser)
//                historyViewModel.addHistory(historyTest)
            },
            navController = navController,
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
private fun CartScreenContent(
    userName: String,
    listProductOfUser: List<Product>,
    cartViewModel: CartViewModel,
    onDeleteProduct: (Int) -> Unit,
    resetProduct: (Boolean) -> Unit,
    onUpdateHistory: (HistoryOfUser) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val listIdProduct = mutableListOf<Int>()
    for (i in listProductOfUser) {
        listIdProduct.add(i.id)
    }
    var showIncorrectDialog by remember { mutableStateOf(false) }
    var showCorrectDialog by remember { mutableStateOf(false) }
    val counts = mutableMapOf<Product, Int>()

    // get number of one product
    for (item in listProductOfUser) {
        if (item in counts) {
            counts[item] = counts[item]!! + 1
        } else {
            counts[item] = 1
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(8f)
        ) {
            items(listProductOfUser.size) { index ->
                var noProductBefore by remember { mutableStateOf(true) }

                for (i in index - 1 downTo 0) {
                    if (listProductOfUser[index].id == listProductOfUser[i].id) {
                        noProductBefore = false
                        break
                    } else {
                        noProductBefore = true
                    }
                }

                if (noProductBefore) {
                    CartItem(
                        userName = userName,
                        product = listProductOfUser[index],
                        numberProduct = counts[listProductOfUser[index]] ?: 1,
                        listIdOfProducts = listIdProduct,
                        cartViewModel = cartViewModel,
                        onDeleteProduct = {
                            onDeleteProduct(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .padding(4.dp),
                    )
                }
            }
        }

        Column(
            Modifier
                .padding(12.dp)
                .background(Color.White)
                .weight(3f),
            verticalArrangement = Arrangement.Bottom
        ) {

            var price = 0.0
            for (i in listProductOfUser) {
                price += i.price
            }
            val discount = price * 0.4
            val total = price - discount

            val priceFormat = (price * 100).toInt() / 100.00
            val discountFormat = (discount * 100).toInt() / 100.00
            val totalFormat = (total * 100).toInt() / 100.00

            Box(
                modifier = Modifier
                    .height(2.dp)
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                androidx.compose.material.Text("Price")
                androidx.compose.material.Text(
                    text = "$$priceFormat",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                androidx.compose.material.Text(text = "Black Friday -40%")
                androidx.compose.material.Text(
                    text = "-$$discountFormat", color = Color.Red,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(5.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                androidx.compose.material.Text(text = "Total")
                androidx.compose.material.Text(
                    text = "$$totalFormat", color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))


            val today = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.now().toString()
            } else {
                TODO("VERSION.SDK_INT < O")
            }

            val historyOfUser = HistoryOfUser(
                date = today,
                price = totalFormat
            )

            Button(
                onClick = {
                    if(totalFormat != 0.0) {
                        showCorrectDialog = true
//                        onUpdateHistory(historyOfUser)
                        navController.navigate("${Screens.Address.route}/$userName/$totalFormat") // ===> Go to addressScreen
                        resetProduct(true)
                    }else {
                        showIncorrectDialog = true
                    }
                },
                shape = RoundedCornerShape(8),
            ) {
                androidx.compose.material.Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = "Checkout",
                    textAlign = TextAlign.Center
                )
            }
        }
    }


    if(showIncorrectDialog) {
        InCorrectDialog {
            showIncorrectDialog = false
        }
    }
}

@Composable
fun InCorrectDialog(
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error for checkout",
                color = Color.Red,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Image(
                painter = painterResource(id = R.drawable.incorrect),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}

@Composable
fun CorrectCheckoutDialog(
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Correct checkout",
                color = Color(78,132,62),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Image(
                painter = painterResource(id = R.drawable.correct),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}

@Composable
fun CartItem(
    userName: String,
    product: Product,
    listIdOfProducts: List<Int>,
    cartViewModel: CartViewModel,
    numberProduct: Int,
    onDeleteProduct: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val listTest = mutableListOf<Int>()
    var cart by remember { mutableStateOf(Cart()) }
    for (i in listIdOfProducts) {
        listTest.add(i)
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = 3.dp
    ) {
        Row {
            Image(
                painter = rememberImagePainter(
                    request = ImageRequest.Builder(LocalContext.current)
                        .data(product.image)
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
                    .fillMaxHeight(),
                contentScale = ContentScale.Inside
            )
            Spacer(modifier = Modifier.width(5.dp))
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = product.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(7f)
                    )

                    TextButton(onClick = {
                        listTest.removeAll({ it == product.id }) // ===> Remove product in list
                        cart = Cart(
                            userName,
                            listTest
                        )
                        cartViewModel.addCart(cart) // ====> Update listProduct after remove
                    },
                        modifier = Modifier.weight(3f)) {
                        Text(text = "Delete", color = Color.Red, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                androidx.compose.material.Text(
                    text = "$${product.price}",
                    color = Color.Black,
                    fontSize = 18.sp,
                    maxLines = 3,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.height(5.dp))
                androidx.compose.material.Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.End,
                    text = "x$numberProduct",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}