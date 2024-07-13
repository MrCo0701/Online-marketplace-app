package com.example.joomina

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.joomina.controller.model.ProductViewModel
import com.example.joomina.controller.model.ShareViewModel
import com.example.joomina.data.Account
import com.example.joomina.data.Comment
import com.example.joomina.data.Product

@Composable
fun TestScreen(
    shareViewModel: ShareViewModel,
    productViewModel: ProductViewModel
) {
    var text by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf(Comment()) }
    var product by remember { mutableStateOf(Product()) }
    var account by remember { mutableStateOf(Account()) }

    shareViewModel.retrieveData(userName = "hao", data = {
        account = it
    })

    productViewModel.retrieveData(id = 32, data = {
        product = it
    })

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = product.title)

        Button(onClick = {
            shareViewModel.addAccount(Account(id = 1, userName = "hao"))
        }) {
            Text(text = "Add comment")
        }
    }
}