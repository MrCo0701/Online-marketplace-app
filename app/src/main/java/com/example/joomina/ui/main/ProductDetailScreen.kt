package com.example.joomina.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.joomina.R
import com.example.joomina.controller.model.CartViewModel
import com.example.joomina.controller.model.LikeViewModel
import com.example.joomina.controller.model.ProductViewModel
import com.example.joomina.controller.model.ShareViewModel
import com.example.joomina.data.Account
import com.example.joomina.data.Cart
import com.example.joomina.data.Comment
import com.example.joomina.data.Like
import com.example.joomina.data.Product
import com.example.joomina.nav.Screens
import com.example.joomina.theme.GrayColor
import com.example.joomina.theme.MainWhiteColor
import com.example.joomina.theme.YellowMain

@Composable
fun ProductDetailScreen(
    userName: String,
    idProduct: String,
    cartViewModel: CartViewModel,
    productViewModel: ProductViewModel,
    likeViewModel: LikeViewModel,
    shareViewModel: ShareViewModel,
    navController: NavController
) {

    // Product
    var product by remember {
        mutableStateOf(Product())
    }
    productViewModel.retrieveData(idProduct.toInt(), data = {
        product = it
    })

    val listIdProduct = mutableListOf<Int>()

    var cart by remember { mutableStateOf(Cart()) }
    cartViewModel.retrieveData(userName = userName, data = { cart = it })
    cart.listProduct.forEach {
        listIdProduct.add(it)
    }

    listIdProduct.add(product.id)


    //LikeData
    var likeOfUser by remember { mutableStateOf(Like()) }
    likeViewModel.retrieveData(userName = userName, data = {
        likeOfUser = it
    })
    val listIdOfLike = mutableListOf<Int>()
    var isIconChecked by remember { mutableStateOf(false) }

    for(i in likeOfUser.listIdProduct) {
        listIdOfLike.add(i)
        if(idProduct.toInt() == i) {
            isIconChecked = true
        }
    }

    //Account
    var account by remember { mutableStateOf(Account()) }
    shareViewModel.retrieveData(userName = userName, data = {
        account = it
    })

    Scaffold(
        backgroundColor = Color.White,
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        navController.navigate("${Screens.MainScreen.route}/$userName")
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_chevron_left),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }


                IconButton(
                    onClick = {
                        isIconChecked = !isIconChecked
                        if (isIconChecked) {
                            listIdOfLike.add(product.id)
                            val likeTest = Like(userName = userName, listIdOfLike)
                            likeViewModel.addLikeProduct(likeTest)
                        }else {
                            listIdOfLike.remove(product.id)
                            val likeTest = Like(userName = userName, listIdOfLike)
                            likeViewModel.addLikeProduct(likeTest)
                        }
                    },
                ) {
                    Icon(
                        painter = if (isIconChecked) {
                            painterResource(id = R.drawable.ic_heart_fill)
                        } else {
                            painterResource(id = R.drawable.ic_heart)
                        },
                        tint = if (isIconChecked) {
                            YellowMain
                        } else {
                            GrayColor
                        },
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        },
        modifier = Modifier.padding(top = 35.dp, bottom = 10.dp)
    ) {
        val i = it
        DetailsScreenContent(
            account = account,
            product = product,
            listIdProduct = listIdProduct,
            productViewModel = productViewModel,
            cartViewModel = cartViewModel,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
fun DetailsScreenContent(
    account: Account,
    product: Product,
    listIdProduct: List<Int>,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    modifier: Modifier = Modifier,
) {
    var textComment by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf(Comment()) }
    var idComment by remember { mutableStateOf(0) }
    var listComment by remember { mutableStateOf(emptyList<Comment>()) }
    productViewModel.getAllComment(product, listComment = {
        listComment = it
    })
    var error by remember { mutableStateOf("") }

    listComment.forEach {
        if (idComment < it.id) {
            idComment = it.id
        }
    }

    Column {
        Box(modifier = modifier.weight(1f), contentAlignment = Alignment.Center) {
            Image(
                painter = rememberImagePainter(
                    request = ImageRequest.Builder(LocalContext.current)
                        .data(product.image)
                        .apply {
                            placeholder(R.drawable.ic_placeholder)
                            crossfade(true)
                        }
                        .build()
                ),
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.Inside
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = modifier
                .fillMaxWidth()
                .weight(2f),
            elevation = 0.dp,
            shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp),
            backgroundColor = MainWhiteColor
        ) {

            Box(
                modifier = modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = product.title,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .weight(2f)
                            .padding(16.dp)
                    )

                    Column(
                        modifier = modifier
                            .padding(16.dp)
                            .weight(7f),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = "Comment",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .weight(2f)
                        )

                        LazyColumn(
                            modifier = Modifier.weight(5f)
                        ) {
                            items(listComment.size) { index ->
                                CommentItem(
                                    comment = listComment[index],
                                    modifier = Modifier.padding(bottom = 10.dp)
                                )
                            }
                        }

                        if(error != "") {
                            Text(text = error)
                        }

                        TextField(
                            value = textComment,
                            onValueChange = {
                                textComment = it
                            },
                            placeholder = {
                                Text(text = "Write something...")
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color(235, 234, 234, 255),
                                textColor = Color.Black,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(2f),
                            shape = RoundedCornerShape(30.dp),
                            trailingIcon = {
                                Icon(
                                    Icons.Filled.Send,
                                    contentDescription = null,
                                    modifier = Modifier.clickable {
                                        comment = Comment(
                                            account = account,
                                            value = textComment,
                                            id = ++idComment
                                        )
                                        if (textComment != "") {
                                            textComment = ""
                                            productViewModel.addComment(
                                                product = product,
                                                comment = comment
                                            )
                                            listComment += comment
                                        }
                                    }
                                )
                            }
                        )
                    }

                    Row(
                        modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .weight(2f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "$${product.price}",
                            color = Color.Black,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = {
                                val cart = Cart(account.userName, listIdProduct)
                                cartViewModel.addCart(cart)
                            },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.Black,
                                backgroundColor = YellowMain
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                text = "Add to cart"
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun CommentItem(
    comment: Comment,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (comment.account.image != "") {
                    Image(
                        painter = rememberImagePainter(
                            request = ImageRequest.Builder(LocalContext.current)
                                .data(comment.account.image)
                                .apply {
                                    placeholder(R.drawable.ic_placeholder) // Placeholder trong khi tải
                                    crossfade(true) // Hiệu ứng chuyển đổi mượt
                                }
                                .build()
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(CircleShape)
                            .size(20.dp),
                        contentScale = ContentScale.Inside
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(5.dp)
                            .clip(CircleShape)
                            .size(20.dp),
                        contentScale = ContentScale.Inside
                    )
                }
                Text(
                    text = comment.account.userName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = comment.value,
                modifier = Modifier.padding(start = 7.dp)
            )
        }
    }
}