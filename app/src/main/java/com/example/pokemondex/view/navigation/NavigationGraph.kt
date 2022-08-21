package com.example.pokemondex.view.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.pokemondex.view.add.AddEvolutionContainer
import com.example.pokemondex.view.add.PokemonAddContainer
import com.example.pokemondex.view.detail.DetailScreen
import com.example.pokemondex.view.home.HomeScreen
import com.example.pokemondex.view.list.PokemonListScreen
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
            enterTransition = { slideInVertically(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            HomeScreen(routeAction = routAction)
        }
        /** 포켓몬 추가 화면 **/
        composable(
            route = RouteAction.Add,
            enterTransition = { slideInVertically(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            PokemonAddContainer(routeAction = routAction)
        }
        /** 포켓몬 진화 추가 화면 **/
        composable(
            route = RouteAction.AddEvolution,
            enterTransition = { slideInVertically(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            AddEvolutionContainer(routeAction = routAction)
        }
        /** 리스트 화면 **/
        composable(
            route = "${RouteAction.List}/{group}",
            arguments = listOf(
                navArgument("group") { type = NavType.StringType }
            ),
            enterTransition = { slideInVertically(animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            PokemonListScreen(routeAction = routAction)
        }
        /** 포켓몬 상세 화면 **/
        composable(
            route = "${RouteAction.Detail}/{number}={isShiny}",
            arguments = listOf(
                navArgument("number") { type = NavType.StringType },
                navArgument("isShiny") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 1000 },
                    animationSpec = spring(stiffness = Spring.StiffnessMedium)
                )
            },
            exitTransition = { fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            DetailScreen(routeAction = routAction)
        }
        /** 포켓몬 상세 화면 : 이전 버튼 클릭 **/
        composable(
            route = "${RouteAction.Detail}/before/{number}={isShiny}",
            arguments = listOf(
                navArgument("number") { type = NavType.StringType },
                navArgument("isShiny") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            ),
            enterTransition = {
                slideInHorizontally(animationSpec = spring(stiffness = Spring.StiffnessMedium))
            },
            exitTransition = { fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            DetailScreen(routeAction = routAction)
        }
    }

}

class RouteAction(private val navController: NavController) {

    fun navToList(group: String) {
        navController.navigate("$List/$group")
    }

    fun navToDetail(
        number: String,
        needPopupBackStack: Boolean = false,
        isShiny: Boolean = false
    ) {
        navController.navigate("$Detail/$number=$isShiny") {
            if (needPopupBackStack) {
                popupBackStack()
            }
        }
    }

    fun navToDetailBefore(
        number: String,
        needPopupBackStack: Boolean = false,
        isShiny: Boolean = false
    ) {
        navController.navigate("$Detail/before/$number=$isShiny") {
            if (needPopupBackStack) {
                popupBackStack()
            }
        }
    }

    fun navToAdd() {
        navController.navigate(Add)
    }

    fun navToAddEvolution() {
        navController.navigate(AddEvolution)
    }

    fun popupBackStack() {
        navController.popBackStack()
    }

    companion object {
        const val Home = "home"
        const val Detail = "detail"
        const val Add = "add"
        const val AddEvolution = "add_evolution"
        const val List = "List"
    }

}