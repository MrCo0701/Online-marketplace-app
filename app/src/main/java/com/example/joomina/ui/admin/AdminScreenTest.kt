package com.example.joomina.ui.admin

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.joomina.R
import com.example.joomina.controller.model.CategoryViewModel
import com.example.joomina.controller.model.ProductViewModel
import com.example.joomina.data.Product
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream


data class User(
    val pass: String,
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreenTest(productViewModel: ProductViewModel, categoryViewModel: CategoryViewModel) {
    val database = Firebase.database
    var isUploading by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf(0) }
    var price by remember { mutableStateOf(0.0) }
    var imgUrl by remember { mutableStateOf("") }
    var i by remember { mutableStateOf(0) }

    var listAccount by remember {
        mutableStateOf(emptyList<Product>())
    }

    productViewModel.getAllProduct {
        listAccount = it
    }

    listAccount.forEach { product ->
        if (product.id > i) {
            i = product.id
        }
    }

    val context = LocalContext.current
    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

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

    val cLaucher = rememberLauncherForActivityResult( // mo may anh
        contract = ActivityResultContracts.TakePicturePreview()
    ) {
        bitmap = it
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp)
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap?.asImageBitmap()!!,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Blue)
                    .size(150.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Blue,
                        shape = CircleShape
                    )
                    .clickable { showDialog = true }
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_home),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Blue)
                    .size(150.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Blue,
                        shape = CircleShape
                    )
                    .clickable { showDialog = true }
            )
        }
    }

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp)
    ) {
        if (showDialog == true) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .width(300.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Blue)
            ) {
                Column(
                    modifier = Modifier.padding(start = 60.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_basket),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                cLaucher.launch()
                                showDialog = false
                            }
                    )

                    Text(text = "Camera")
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column(
                    modifier = Modifier.padding(start = 60.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_home),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                laucher.launch("image/*")
                                showDialog = false
                            }
                    )

                    Text(text = "Picture")
                }

                Column(
                    modifier = Modifier
                        .padding(start = 50.dp, bottom = 80.dp)
                ) {
                    Text(
                        text = "X",
                        color = Color.White,
                        modifier = Modifier.clickable {
                            showDialog = false
                        }
                    )
                }
            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 300.dp)
    ) {
        TextField(
            label = {
                    Text(text = "Enter title")
            },
            value = title,
            onValueChange = {
                title = it
            },
            modifier = Modifier.padding(bottom = 10.dp)
        )

        TextField(
            label = {
                Text(text = "Enter description")
            },
            value = description,
            onValueChange = {
                description = it
            },
            modifier = Modifier.padding(bottom = 10.dp)
        )

        TextField(
            label = {
                Text(text = "Enter price")
            },
            value = price.toString(),
            onValueChange = {
                price = it.toDouble()
            },
            modifier = Modifier.padding(bottom = 10.dp)
        )

        TextField(
            label = {
                Text(text = "Enter categoryid")
            },
            value = categoryId.toString(),
            onValueChange = {
                categoryId = it.toInt()
            },
            modifier = Modifier.padding(bottom = 10.dp)
        )




        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                isUploading = true
                bitmap.let { bitmap ->
                    if (bitmap != null) {
                        uploadingImageToFireBase(
                            bitmap,
                            context as ComponentActivity
                        ) { success, imageUrl ->
                            isUploading = false
                            if (success) {
                                imageUrl?.let {
                                    imgUrl = it
                                }

                                val product = Product(
                                    id = ++i,
                                    title,
                                    price,
                                    categoryId,
                                    description,
                                    imgUrl
                                )

                                productViewModel.addProduct(product)

                                Toast.makeText(context, "Update user", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Not update user", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        ) {
            Text(
                text = "Sign up",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(330.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isUploading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(16.dp),
                color = Color.White
            )
        }
    }
}

fun uploadingImageToFireBase(
    bitmap: Bitmap,
    context: ComponentActivity,
    callBack: (Boolean, String) -> Unit
) {
    val storage = Firebase.storage.reference
    val imageRef = storage.child("imageProduct/${bitmap}")

    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos) //  luu bitmap vao baos
    val imageData = baos.toByteArray()

    imageRef.putBytes(imageData).addOnSuccessListener {
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()
            callBack(true, imageUrl)
        }.addOnFailureListener {
            callBack(false, null.toString())
        }
    }.addOnFailureListener {
        callBack(false, null.toString())
    }
}
