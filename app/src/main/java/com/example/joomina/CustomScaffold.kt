package com.example.joomina

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.joomina.nav.Screens
import com.example.joomina.theme.DarkBlue
import com.example.joomina.theme.GrayColor
import com.example.joomina.theme.MainWhiteColor

data class MainScreenChange(
    val selectedIcon: Int,
    val routeName: String
)

@Composable
fun CustomScaffold(
    userName: String,
    navController: NavController,
    showBottomBar: Boolean,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    val items: List<MainScreenChange> = listOf(
        MainScreenChange(
            R.drawable.ic_home,
            Screens.MainScreen.route
        ),
        MainScreenChange(
            R.drawable.ic_basket,
            Screens.CartScreen.route
        ),
        MainScreenChange(
            R.drawable.ic_heart,
            Screens.LikeScreen.route
        ),
        MainScreenChange(
            R.drawable.ic_user,
            Screens.UserScreen.route
        )
    )

    var selectItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigation(
                    backgroundColor = MainWhiteColor,
                    elevation = 5.dp,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
//                    val currentDestination = navBackStackEntry?.destination
                    items.forEachIndexed { index, item ->
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    painter = painterResource(id = item.selectedIcon),
                                    contentDescription = null
                                )
                            },
                            selectedContentColor = DarkBlue,
                            unselectedContentColor = GrayColor,
                            selected = selectItemIndex == index,
                            onClick = {
                                navController.navigate("${item.routeName}/$userName")
//                                navController.navigate(item.destination.route) {
//                                    navController.graph.startDestinationRoute?.let { screen_route ->
//                                        popUpTo(screen_route) {
//                                            saveState = true
//                                        }
//                                    }
//                                    launchSingleTop = true
//                                    restoreState = true
//                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}