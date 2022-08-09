package com.example.pokemondex.view.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.pokemondex.view.add.PokemonAddContainer
import com.example.pokemondex.view.detail.DetailContainer
import com.example.pokemondex.view.home.HomeContainer
import com.example.pokemondex.view.list.PokemonListContainer
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
        /** 리스트 화면 **/
        composable(
            route = "${RouteAction.List}/{group}",
            arguments = listOf(
                navArgument("group") { type = NavType.StringType }
            ),
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            PokemonListContainer(routeAction = routAction)
        }
        /** 포켓몬 상세 화면 **/
        composable(
            route = "${RouteAction.Detail}/{number}",
            arguments = listOf(
                navArgument("number") { type = NavType.StringType },
            ),
            enterTransition = { scaleIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { scaleOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ){
            DetailContainer(routeAction = routAction)
        }
    }

}

class RouteAction(private val navController: NavController) {

    fun navToList(group: String) {
        navController.navigate("$List/$group")
    }

    fun navToDetail(number: String) {
        navController.navigate("$Detail/$number")
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
        const val List = "List"
    }

}