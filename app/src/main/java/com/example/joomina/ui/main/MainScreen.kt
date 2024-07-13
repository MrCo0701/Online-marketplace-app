package com.example.joomina.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.joomina.R
import com.example.joomina.controller.model.CartViewModel
import com.example.joomina.controller.model.CategoryViewModel
import com.example.joomina.controller.model.ProductViewModel
import com.example.joomina.controller.model.ShareViewModel
import com.example.joomina.data.Account
import com.example.joomina.data.Cart
import com.example.joomina.data.Categories
import com.example.joomina.data.Product
import com.example.joomina.nav.Screens
import com.example.joomina.theme.DarkBlue
import com.example.joomina.theme.MainWhiteColor
import com.example.joomina.theme.YellowMain


@Composable
fun MainScreen(
    userName: String,
    productViewModel: ProductViewModel,
    categoryViewModel: CategoryViewModel,
    shareViewModel: ShareViewModel,
    cartViewModel: CartViewModel,
    navController: NavController,
) {
    var productSearch by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            MyTopAppBar(
                userName = userName,
                shareViewModel = shareViewModel,
                onSearchClick = {
                    productSearch = it
                }
            )
        }
    ) {
        val t = it
        if (productSearch != "") {
            HomeScreenContent(
                userName = userName,
                productViewModel = productViewModel,
                categoryViewModel = categoryViewModel,
                searchItem = productSearch,
                navController = navController,
                cartViewModel = cartViewModel,

            )
        } else {
            HomeScreenContent(
                userName = userName,
                productViewModel = productViewModel,
                categoryViewModel = categoryViewModel,
                navController = navController,
                cartViewModel = cartViewModel,

            )
        }
    }
}

@Composable
fun HomeScreenContent(
    userName: String,
    productViewModel: ProductViewModel,
    categoryViewModel: CategoryViewModel,
    cartViewModel: CartViewModel,
    navController: NavController,
    searchItem: String? = null
) {
    //product
    var listProduct by remember { mutableStateOf(emptyList<Product>()) }
    val list = mutableListOf<Product>()
    productViewModel.getAllProduct { listProduct = it }
    var categoryId by remember { mutableStateOf(99) }


    //Cart
    val listIdProduct = mutableListOf<Int>()
    var listCart by remember { mutableStateOf(emptyList<Cart>()) }
    cartViewModel.getAllCart {
        listCart = it
    }

    var cart by remember { mutableStateOf(Cart()) }
    cartViewModel.retrieveData(userName = userName, data = { cart = it })
    cart.listProduct.forEach {
        listIdProduct.add(it)
    }

    if (categoryId == 99) {
        if (searchItem != null) {
            listProduct.forEach { product ->
                if (product.title.contains(searchItem, ignoreCase = true)) {
                    list.add(product)
                }
            }

            listProduct = list
        }
    } else {
        if (searchItem != null) {
            listProduct.forEach { product ->
                if (product.title.contains(
                        searchItem,
                        ignoreCase = true
                    ) && product.categoryId == categoryId
                ) {
                    list.add(product)
                }
            }

            listProduct = list
        } else {
            listProduct.forEach { product ->
                if (product.categoryId == categoryId) {
                    list.add(product)
                }
            }

            listProduct = list
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 160.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(20.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.blackfriday),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Black Friday Banner",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }

            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item(span = { GridItemSpan(2) }) {
                Categories(categoryViewModel = categoryViewModel) {
                    categoryId = it
                }
            }

            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(listProduct.size) { index ->
                ProductItem(
                    userName = userName,
                    listProduct[index],
                    navController = navController,
                    cartViewModel = cartViewModel,
                    listProductInCart = listIdProduct,
                    modifier = Modifier.width(150.dp)
                )
            }
        }
    }
}


@Composable
private fun ProductItem(
    userName: String,
    product: Product,
    navController: NavController,
    cartViewModel: CartViewModel,
    listProductInCart: List<Int>,
    modifier: Modifier = Modifier
) {
    val listproductinCart = mutableListOf<Int>()
    listProductInCart.forEach {
        listproductinCart.add(it)
    }

    listproductinCart.add(product.id)

    androidx.compose.material.Card(
        modifier = modifier
            .padding(4.dp)
            .clickable {
                navController.navigate("${Screens.ProductDetailScreen.route}/$userName/${product.id}")
            },
        backgroundColor = Color.White,
        elevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.Start,
        ) {
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
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Inside
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top,
            ) {
                Icon(
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = null,
                    tint = YellowMain
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "4 (50)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$${product.price}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            OutlinedButton(
                // ADD PRODUCT
                onClick = {
                    val cart = Cart(userName = userName, listProduct = listproductinCart)

                    cartViewModel.addCart(cart) // ==> Add product to cart
                },
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.End)
                    .background(YellowMain, shape = RoundedCornerShape(50.dp)),
                shape = CircleShape,
                border = BorderStroke(0.dp, Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = null,
                    tint = MainWhiteColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    userName: String,
    shareViewModel: ShareViewModel,
    onSearchClick: (String) -> Unit
) {
    var search by remember { mutableStateOf("") }
    var account by remember { mutableStateOf(Account()) }
    shareViewModel.retrieveData(userName = userName, data = {
        account = it
    })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 40.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if(account.image == "") {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(35.dp),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }else {
                    Image(
                        painter = rememberImagePainter(
                            request = ImageRequest.Builder(LocalContext.current)
                                .data(account.image)
                                .apply {
                                    placeholder(R.drawable.ic_placeholder) // Placeholder for loading
                                    crossfade(true) // animation clean
                                }
                                .build()
                        ),
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(35.dp),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Hi, $userName", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Icon(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(id = R.drawable.ic_allert),
                contentDescription = null,
                tint = DarkBlue
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = search,
                onValueChange = {
                    search = it
                },
                placeholder = {
                    Text(
                        text = "Search",
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(0.80f)
                    .background(MainWhiteColor, shape = RoundedCornerShape(8.dp))
                    .clickable {

                    },
                shape = RoundedCornerShape(size = 8.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = true,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onSearchClick(search)
                    }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = MainWhiteColor,
                    containerColor = MainWhiteColor,
                    focusedIndicatorColor = MainWhiteColor,
                    unfocusedIndicatorColor = MainWhiteColor,
                    disabledIndicatorColor = MainWhiteColor
                ),
                textStyle = TextStyle(color = Color.Black),
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        modifier = Modifier
                            .size(24.dp),
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        tint = DarkBlue
                    )
                }
            )

            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    modifier = Modifier
                        .size(55.dp)
                        .clip(
                            shape = RoundedCornerShape(
                                size = 8.dp
                            )
                        )
                        .background(
                            MainWhiteColor
                        )
                        .padding(
                            start = 4.dp,
                            end = 4.dp,
                            top = 4.dp,
                            bottom = 4.dp
                        ),
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = null,
                    tint = DarkBlue
                )
            }
        }
    }
}


@Composable
fun Categories(categoryViewModel: CategoryViewModel, onClickFilt: (Int) -> Unit) {
    var list by remember { mutableStateOf(emptyList<Categories>()) }
    var selectedItemIndex by remember { mutableStateOf(0) }

    categoryViewModel.getAllCategories { // ===> GET ALL CATEGORIES
            list = it
    }

    val listTest = mutableListOf(Categories(name = "All", id = 99))
    for (i in list) {
        listTest.add(i)
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(listTest.size) { index ->
            Text(
                text = listTest[index].name,
                color = Color.Black,
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(
                            size = 8.dp,
                        ),
                    )
                    .clickable {
                        selectedItemIndex = index
                        onClickFilt(listTest[index].id)
                    }
                    .background(
                        if (index == selectedItemIndex) {
                            YellowMain
                        } else {
                            MainWhiteColor
                        }
                    )
                    .padding(10.dp)
            )
        }
    }
}