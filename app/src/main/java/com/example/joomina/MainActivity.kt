package com.example.joomina

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.joomina.controller.model.CartViewModel
import com.example.joomina.controller.model.CategoryViewModel
import com.example.joomina.controller.model.HistoryViewModel
import com.example.joomina.controller.model.LikeViewModel
import com.example.joomina.controller.model.ProductViewModel
import com.example.joomina.controller.model.ShareViewModel
import com.example.joomina.nav.NavGraph
import com.example.joomina.theme.JoominaTheme
import com.example.joomina.ui.admin.AdminScreen
import com.example.joomina.ui.main.UserScreen

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val sharedViewModel: ShareViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()
    private val likeViewModel: LikeViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JoominaTheme {
                var showBottomBar by remember { mutableStateOf(false) }
                var userName by remember { mutableStateOf("") }
                
                navController = rememberNavController()

                CustomScaffold(
                    navController = navController,
                    showBottomBar = showBottomBar,
                    userName = userName
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavGraph(
                            paddingValues = innerPadding,
                            navController = navController,
                            shareViewModel = sharedViewModel,
                            categoryViewModel = categoryViewModel,
                            productViewModel = productViewModel,
                            historyViewModel = historyViewModel,
                            cartViewModel = cartViewModel,
                            likeViewModel = likeViewModel,
                            showButtonBar = { showBottomBar = it },
                            showUserName = { userName = it }
                        )
                    }
                }

            }
        }
    }
}