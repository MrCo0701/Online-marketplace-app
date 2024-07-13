package com.example.joomina.nav

sealed class Screens (
    val route: String
){
    object LoginScreen: Screens(route = "login")
    object SignupScreen: Screens(route = "signup")
    object StartScreen: Screens(route = "start")
    object MainScreen : Screens(route = "main")
    object LikeScreen : Screens(route = "like")
    object CartScreen: Screens(route = "cart")
    object UserScreen: Screens(route = "user")
    object ProductDetailScreen: Screens(route = "productDetail")
    object AdminScreen: Screens(route = "admin")
    object addProductScreen: Screens(route = "addProduct")
    object TotalUserScreen: Screens(route = "totalUser")
    object TotalOrder: Screens(route = "totalOrder")
    object Address: Screens(route = "address")
}