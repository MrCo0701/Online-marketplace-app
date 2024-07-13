package com.example.joomina.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.joomina.controller.model.CartViewModel
import com.example.joomina.controller.model.CategoryViewModel
import com.example.joomina.controller.model.HistoryViewModel
import com.example.joomina.controller.model.LikeViewModel
import com.example.joomina.controller.model.ProductViewModel
import com.example.joomina.controller.model.ShareViewModel
import com.example.joomina.ui.admin.AddProductScreen
import com.example.joomina.ui.admin.AdminScreen
import com.example.joomina.ui.admin.TotalOrder
import com.example.joomina.ui.admin.TotalUserScreen
import com.example.joomina.ui.login.screen.LoginScreen
import com.example.joomina.ui.login.screen.SignupScreen
import com.example.joomina.ui.login.screen.StartScreen
import com.example.joomina.ui.main.AddressScreen
import com.example.joomina.ui.main.CartScreen
import com.example.joomina.ui.main.LikeScreen
import com.example.joomina.ui.main.MainScreen
import com.example.joomina.ui.main.ProductDetailScreen
import com.example.joomina.ui.main.UserScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    paddingValues: PaddingValues,
    navController: NavHostController,
    shareViewModel: ShareViewModel,
    historyViewModel: HistoryViewModel,
    categoryViewModel: CategoryViewModel,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    likeViewModel: LikeViewModel,
    showButtonBar: (Boolean) -> Unit,
    showUserName: (String) -> Unit
) {
    NavHost(navController = navController, startDestination = Screens.StartScreen.route) {
        composable(route = Screens.StartScreen.route) {
            StartScreen(navController = navController)
        }

        composable(route = Screens.LoginScreen.route) {
            LoginScreen(
                shareViewModel = shareViewModel,
                navController = navController,
                showButtonBar = {
                    showButtonBar(it)
                },
                getUserName = {
                    showUserName(it)
                }
            )
        }

        composable(route = Screens.SignupScreen.route) {
            SignupScreen(navController = navController, shareViewModel = shareViewModel)
        }

        composable(
            route = "${Screens.MainScreen.route}/{userName}",
            arguments = listOf(
                navArgument(name = "userName") {
                    type = NavType.StringType
                }
            )
        ) {
            MainScreen(
                userName = it.arguments?.getString("userName").toString(),
                productViewModel = productViewModel,
                cartViewModel = cartViewModel,
                categoryViewModel = categoryViewModel,
                shareViewModel = shareViewModel,
                navController = navController
            )
        }

        composable(route = "${Screens.LikeScreen.route}/{userName}") {
            LikeScreen(
                userName = it.arguments?.getString("userName").toString(),
                likeViewModel = likeViewModel,
                productViewModel = productViewModel
            )
        }

        composable(route = "${Screens.UserScreen.route}/{userName}") {
            UserScreen(
                userName = it.arguments?.getString("userName").toString(),
                shareViewModel =  shareViewModel,
                historyViewModel = historyViewModel,
                cartViewModel = cartViewModel,
                likeViewModel = likeViewModel,
                navController = navController,
                showButtonBar = {showButtonBar(it)}
            )
        }

        composable(
            route = "${Screens.CartScreen.route}/{userName}",
            arguments = listOf(
                navArgument(name = "userName") {
                    type = NavType.StringType
                }
            )
        ) {
            CartScreen(
                userName = it.arguments?.getString("userName").toString(),
                shareViewModel = shareViewModel,
                historyViewModel = historyViewModel,
                productViewModel = productViewModel,
                cartViewModel = cartViewModel,
                navController = navController
            )
        }

        composable(
            route = "${Screens.ProductDetailScreen.route}/{userName}/{idProduct}",
            arguments = listOf(
                navArgument("idProduct") {
                    type = NavType.IntType
                },
                navArgument(name = "userName") {
                    type = NavType.StringType
                }
            )
        ) {
            ProductDetailScreen(
                userName = it.arguments?.getString("userName").toString(),
                idProduct = it.arguments?.getInt("idProduct").toString(),
                cartViewModel = cartViewModel,
                productViewModel = productViewModel,
                likeViewModel = likeViewModel,
                shareViewModel = shareViewModel,
                navController = navController
            )
        }

        composable(route = Screens.AdminScreen.route) {
            AdminScreen(
                productViewModel = productViewModel,
                historyViewModel = historyViewModel,
                shareViewModel = shareViewModel,
                navController = navController
            )
        }

        composable(route = Screens.addProductScreen.route) {
            AddProductScreen(
                productViewModel = productViewModel,
                categoryViewModel = categoryViewModel,
                navController = navController
            )
        }

        composable(route = Screens.TotalUserScreen.route) {
            TotalUserScreen(
                shareViewModel = shareViewModel,
                historyViewModel = historyViewModel,
                cartViewModel = cartViewModel,
                likeViewModel = likeViewModel,
                navController = navController
            )
        }

        composable(route = Screens.TotalOrder.route) {
            TotalOrder(
                historyViewModel = historyViewModel,
                shareViewModel = shareViewModel,
                navController = navController
            )
        }

        composable(
            route = "${Screens.Address.route}/{userName}/{price}",
            arguments = listOf(
                navArgument("userName") {
                    type = NavType.StringType
                },
                navArgument("price") {
                    type = NavType.StringType
                }
            )
        ) {
            AddressScreen(
                paddingValues = paddingValues,
                userName = it.arguments?.getString("userName").toString(),
                price = it.arguments?.getString("price").toString().toDouble(),
                historyViewModel = historyViewModel,
                navController = navController
            )
        }
    }
}