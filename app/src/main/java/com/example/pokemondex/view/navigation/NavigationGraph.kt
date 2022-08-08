package com.example.pokemondex.view.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.pokemondex.view.add.PokemonAddContainer
import com.example.pokemondex.view.home.HomeContainer
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationGraph() {
    val navController = rememberAnimatedNavController()
    val routAction = remember(navController) {
        RouteAction(navController)
    }

    AnimatedNavHost(
        navController = navController,
        startDestination = RouteAction.Home
    ) {
        /** 홈 화면 **/
        composable(
            route = RouteAction.Home,
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            HomeContainer(routeAction = routAction)
        }
        /** 포켓몬 추가 화면 **/
        composable(
            route = RouteAction.Add,
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            PokemonAddContainer(routeAction = routAction)
        }
    }

}

class RouteAction(private val navController: NavController) {

    fun navToDetail(group: String) {
        navController.navigate("$Detail/group")
    }

    fun navToAdd() {
        navController.navigate(Add)
    }

    fun popupBackStack() {
        navController.popBackStack()
    }

    companion object {
        const val Home = "home"
        const val Detail = "detail"
        const val Add = "add"
    }

}