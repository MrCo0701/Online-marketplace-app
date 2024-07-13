package com.example.joomina.ui.admin

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.joomina.R
import com.example.joomina.controller.model.CategoryViewModel
import com.example.joomina.controller.model.ProductViewModel
import com.example.joomina.data.Categories
import com.example.joomina.data.Product
import com.example.joomina.theme.YellowMain
import com.example.joomina.ui.main.ButtonCustom
import com.example.joomina.ui.main.TextFieldCustom
import com.example.tanlam.common.isEmptyString

@Composable
fun AddProductScreen(
    productViewModel: ProductViewModel,
    categoryViewModel: CategoryViewModel,
    navController: NavController
) {
    var nameProduct by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var descriptinon by remember { mutableStateOf("") }

    //Category
    var listCategories by remember { mutableStateOf(emptyList<Categories>()) }
    var categorySelected by remember { mutableStateOf(0) }
    categoryViewModel.getAllCategories {
        listCategories = it
    }

    //Product
    var listProduct by remember { mutableStateOf(emptyList<Product>()) }
    var imgUri by remember { mutableStateOf("") }
    var i by remember { mutableStateOf(0) }
    var productTest by remember { mutableStateOf(Product()) }
    productViewModel.getAllProduct {
        listProduct = it
    }
    for(product in listProduct) {
        if(product.id > i) {
            i = product.id
        }
    }


    var showDiaLogChangeProduct by remember { mutableStateOf(false) }

    if (showDiaLogChangeProduct) {
        DiaLogChangeProduct(
            product = productTest,
            listCategories = listCategories,
            productViewModel = productViewModel,
            onDismissRequest = {
                showDiaLogChangeProduct = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(254, 239, 173))
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
                    text = "Add Product",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(8f)
                .background(
                    Color.White, shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var isShowAddProduct by remember { mutableStateOf(false) }
            var errorText by remember { mutableStateOf("") }

            if (isShowAddProduct) {
                val context = LocalContext.current
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

                Card(
                    modifier = Modifier
                        .height(120.dp)
                        .width(160.dp)
                        .clickable {
                            laucher.launch("image/*")
                        }
                        .drawBehind {
                            val strokeWidth = 3.dp.toPx()
                            val pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(4.dp.toPx(), 5.dp.toPx()), 0f
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
                            androidx.compose.material3.Text(text = "Drop images here")
                        }
                    } else {
                        Image(
                            bitmap = bitmap!!.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )

                        productViewModel.uploadImage(
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

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextFieldCustom(title = "Enter product name", onValueChange = {
                        nameProduct = it
                    })


                    DropdownMenuCustom(
                        list = listCategories,
                        onSelectedCategory = {
                            categorySelected = it
                        }
                    )

                    TextFieldCustom(
                        title = "Price",
                        onValueChange = {
                            price = it
                        },
                        numberKeyBoard = true
                    )

                    TextFieldCustom(title = "Description", onValueChange = {
                        descriptinon = it
                    })
                }

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = errorText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Red
                )
                
                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ButtonCustom(
                        title = "Add Product",
                        backGroundColor = Color(243, 202, 82),

                        // ADD PRODUCT
                        onClickButton = {
                            if (isEmptyString(nameProduct)) {
                                errorText = "invalid name of product"
                            } else if (isEmptyString(price.toString())) {
                                errorText = "invalid price of product"
                            } else if (isEmptyString(descriptinon)) {
                                errorText = "invalid des of product"
                            } else if (isEmptyString(imgUri)) {
                                errorText = "invalid image of product"
                            } else {

                                productTest = Product(
                                    id = ++i,
                                    title = nameProduct,
                                    price = price.toDouble(),
                                    categoryId = categorySelected,
                                    descriptinon,
                                    image = imgUri
                                )

                                productViewModel.addProduct(productTest) // ===>  add product to firebase
                                isShowAddProduct = false
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    ButtonCustom(
                        title = "Show products",
                        backGroundColor = Color(255, 95, 0),
                        onClickButton = {
                            isShowAddProduct = false
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Box(modifier = Modifier.weight(1f)) {
                    ButtonCustom(title = "Go to add product",
                        backGroundColor = Color.Gray,
                        onClickButton = {
                            isShowAddProduct = true
                        })
                }
                Box(
                    modifier = Modifier
                        .weight(0.04f)
                        .fillMaxWidth()
                        .background(Color.Gray)
                )

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(128.dp), modifier = Modifier.weight(8f)
                ) {
                    items(listProduct.size) {index ->
                        ProductItemAdmin(
                            listProduct[index],
                            productViewModel = productViewModel,
                            deleteSucces = {
                                listProduct = it
                            },
                            onClickChangeProduct = {
                                productTest = it
                                showDiaLogChangeProduct = true // ==> Show dialog change product
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductItemAdmin(
    product: Product,
    productViewModel: ProductViewModel,
    deleteSucces:(List<Product>) -> Unit,
    onClickChangeProduct: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable {

            },
        backgroundColor = Color.White,
        elevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Image(
                painter  = rememberImagePainter(
                    request = ImageRequest.Builder(LocalContext.current)
                        .data(product.image)
                        .apply {
                            placeholder(R.drawable.ic_placeholder) // Placeholder for loading
                            crossfade(true) // clean animation
                        }
                        .build()
                ),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Inside
            )
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.Text(
                text = product.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            androidx.compose.material3.Text(
                text = "$${product.price}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Color(255, 159, 102), shape = RoundedCornerShape(50.dp)
                        )
                        .padding(5.dp)
                        .clickable {
                            onClickChangeProduct(product)
                        }
                ) {
                    Icon(
                        Icons.Default.Edit, contentDescription = null, tint = Color(255, 95, 0)
                    )
                }

                Box(
                    modifier = Modifier
                        .background(
                            Color(254, 239, 173), shape = RoundedCornerShape(50.dp)
                        )
                        .padding(5.dp)
                        .clickable {
                            productViewModel.deleteData(product.id.toString()) // ===>  Delete product in firebase
                            productViewModel.getAllProduct {
                                deleteSucces(it)
                            }
                        }
                ) {
                    Icon(
                        Icons.Default.Delete, contentDescription = null, tint = Color(243, 202, 82)
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuCustom(
    list: List<Categories>,
    valueIfHave: Int? = null,
    onSelectedCategory: (Int) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var gender by remember { mutableStateOf(list[0].name) }
    var category by remember { mutableStateOf(Categories()) }
    for (i in list) {
        if (i.id == valueIfHave) {
            category = i
        }
    }
    if (valueIfHave != null) {
        gender = category.name
        onSelectedCategory(valueIfHave)
    }

    ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = {
        isExpanded = it
    }) {
        OutlinedTextField(
            value = gender,
            onValueChange = {

            },
            maxLines = 1,
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            for (i in list) {
                DropdownMenuItem(text = {
                    Text(text = i.name)
                }, onClick = {
                    gender = i.name
                    onSelectedCategory(i.id)
                    isExpanded = false
                })
            }
        }
    }
}

@Composable
fun DiaLogChangeProduct(
    product: Product,
    listCategories: List<Categories>,
    productViewModel: ProductViewModel,
    onDismissRequest: () -> Unit
) {
    //Image
    var imgUri by remember { mutableStateOf(product.image) }
    val context = LocalContext.current
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

    //Value
    var nameProduct by remember { mutableStateOf(product.title) }
    var price by remember { mutableStateOf(product.price.toString()) }
    var description by remember { mutableStateOf(product.description) }
    var categorySelected by remember { mutableStateOf(product.categoryId) }

    //Product
    var productChange by remember { mutableStateOf(Product()) }

    var errorText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = {
        onDismissRequest()
    }) {
        Card {
            Column(
                modifier = Modifier.padding(20.dp),
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
                            val pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(4.dp.toPx(), 5.dp.toPx()), 0f
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
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = rememberImagePainter(
                                    request = ImageRequest.Builder(LocalContext.current)
                                        .data(product.image)
                                        .apply {
                                            placeholder(R.drawable.imageupdate) // Placeholder trong khi tải
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

                        productViewModel.uploadImage(
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

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextFieldCustom(
                        title = "Enter product name",
                        onValueChange = {
                            nameProduct = it
                        },
                        valueIfHave = nameProduct
                    )


                    DropdownMenuCustom(
                        list = listCategories,
                        onSelectedCategory = {
                            categorySelected = it
                        }
                    )

                    TextFieldCustom(
                        title = "Price", onValueChange = {
                            price = it
                        },
                        numberKeyBoard = true,
                        valueIfHave = price.toString()
                    )

                    TextFieldCustom(
                        title = "Description", onValueChange = {
                            description = it
                        },
                        valueIfHave = description
                    )

                }

                Text(
                    text = errorText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Red
                )

                Spacer(modifier = Modifier.height(20.dp))
                ButtonCustom(
                    title = "Change",
                    backGroundColor = YellowMain,

                    // CHANGE PRODUCT
                    onClickButton = {
                    if (isEmptyString(nameProduct)) {
                        errorText = "invalid name of product"
                    } else if (isEmptyString(price.toString())) {
                        errorText = "invalid price of product"
                    } else if (isEmptyString(description)) {
                        errorText = "invalid des of product"
                    } else if (isEmptyString(imgUri)) {
                        errorText = "invalid image of product"
                    } else {
                        productChange = Product(
                            id = product.id,
                            title = nameProduct,
                            price = price.toDouble(),
                            categoryId = categorySelected,
                            description = description,
                            image = imgUri
                        )

                        productViewModel.addProduct(productChange) // Add product after change
                        onDismissRequest()
                    }
                })
            }
        }
    }
}
