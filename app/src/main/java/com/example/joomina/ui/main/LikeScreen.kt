package com.example.joomina.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.joomina.R
import com.example.joomina.controller.model.LikeViewModel
import com.example.joomina.controller.model.ProductViewModel
import com.example.joomina.data.Like
import com.example.joomina.data.Product
import com.example.joomina.theme.YellowMain

@Composable
fun LikeScreen(
    userName: String,
    likeViewModel: LikeViewModel,
    productViewModel: ProductViewModel
) {
    var userLike by remember { mutableStateOf(Like()) }
    val listProduct = mutableListOf<Product>()
    likeViewModel.retrieveData(userName = userName, data = {
        userLike = it
    })
    for (i in userLike.listIdProduct) {
        productViewModel.retrieveData(i, data = {
            listProduct.add(it)
        })
    }

    Scaffold(
        backgroundColor = Color.White,
        topBar = {
            TopAppBar(
                elevation = 1.dp,
                backgroundColor = Color.White,
                title = {
                    androidx.compose.material.Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 24.dp),
                        text = "Wishlist",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            likeViewModel.addLikeProduct(Like(userName, listOf())) // ==> Add product to database
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }
            )
        },
        modifier = Modifier.padding(top = 35.dp)
    ) {
        WishListScreenContent(
            userName = userName,
            listProduct = listProduct,
            likeViewModel = likeViewModel,
            modifier = Modifier.padding(it)
        )
    }
}


@Composable
private fun WishListScreenContent(
    userName: String,
    listProduct: List<Product>,
    likeViewModel: LikeViewModel,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn {
            items(listProduct.size) { index ->
                WishlistItem(
                    userName = userName,
                    likeViewModel = likeViewModel,
                    product = listProduct[index],
                    onRemoveProduct = {
                        val listTest = mutableListOf<Int>()
                        for(i in listProduct) {
                            listTest.add(i.id)
                        }

                        listTest.remove(listProduct[index].id)
                        val like = Like(userName, listTest)
                        likeViewModel.addLikeProduct(like)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WishlistItem(
    userName: String,
    product: Product,
    likeViewModel: LikeViewModel,
    onRemoveProduct:() -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = 3.dp,
        onClick = {

        }
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
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(5.dp)
            ) {
                Text(
                    text = product.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(5.dp))
                androidx.compose.material.Text(
                    text = "$${product.price}",
                    color = Color.Black,
                    fontSize = 22.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light
                )
                IconButton(
                    onClick = {
                        onRemoveProduct() // ===> Go to remove action
                    },
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_heart_fill),
                        tint = YellowMain,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}