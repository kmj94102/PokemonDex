package com.example.pokemondex.view.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.*
import com.example.pokemondex.view.add.AddEvolutionContainer
import com.example.pokemondex.view.add.PokemonAddContainer
import com.example.pokemondex.view.detail.DetailScreen
import com.example.pokemondex.view.download.DownloadScreen
import com.example.pokemondex.view.home.HomeScreen
import com.example.pokemondex.view.list.PokemonListScreen
import com.example.pokemondex.view.new_dex.NewDexListScreen
import com.example.pokemondex.view.new_dex.detail.NewDetailScreen
import com.example.pokemondex.view.update.evolution.UpdateEvolutionContainer
import com.example.pokemondex.view.update.pokemon.NewPokemonDexContainer
import com.example.pokemondex.view.update.search.UpdateSearchContainer
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
        customComposable(
            route = RouteAction.Home
        ) {
            HomeScreen(routeAction = routAction)
        }
        /** 포켓몬 추가 화면 **/
        customComposable(
            route = RouteAction.Add
        ) {
            PokemonAddContainer(routeAction = routAction)
        }
        /** 포켓몬 진화 추가 화면 **/
        customComposable(
            route = RouteAction.AddEvolution
        ) {
            AddEvolutionContainer(routeAction = routAction)
        }
        /** 포켓몬 수정을 위한 검색 화면 **/
        customComposable(
            route = "${RouteAction.UpdateSearch}/{type}",
            arguments = listOf(
                navArgument(RouteAction.Type) { type = NavType.StringType }
            )
        ) { scope ->
            val type = scope.arguments?.getString(RouteAction.Type) ?: return@customComposable
            UpdateSearchContainer(routeAction = routAction, type = type)
        }
        /** 포켓몬 진화 수정 화면 **/
        customComposable(
            route = "${RouteAction.UpdateEvolution}/{index}",
            arguments = listOf(
                navArgument("index") { type = NavType.StringType }
            )
        ) {
            UpdateEvolutionContainer(routeAction = routAction)
        }
        /** 신규 도감 등록 화면 **/
        customComposable(
            route = "${RouteAction.NewPokemonDex}/{index}",
            arguments = listOf(
                navArgument("index") { type = NavType.StringType }
            )
        ) {
            NewPokemonDexContainer(routeAction = routAction)
        }
        /** 리스트 화면 **/
        customComposable(
            route = "${RouteAction.List}/{group}",
            arguments = listOf(
                navArgument("group") { type = NavType.StringType }
            )
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
        /** 아르세우스 도감 **/
        customComposable(
            route = RouteAction.ArcusDex,
        ) {
            NewDexListScreen(routAction = routAction)
        }
        /** 아르세우스 도감 상세 **/
        composable(
            route = "${RouteAction.ArcusDetail}/{number}/{allDexNumber}",
            arguments = listOf(
                navArgument("number") { type = NavType.LongType },
                navArgument("allDexNumber") { type = NavType.StringType },
            ),
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 1000 },
                    animationSpec = spring(stiffness = Spring.StiffnessMedium)
                )
            },
            exitTransition = { fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            NewDetailScreen(routeAction = routAction)
        }
        /** 아르세우스 도감 상세 : 이전 버튼 **/
        composable(
            route = "${RouteAction.ArcusDetail}/before/{number}/{allDexNumber}",
            arguments = listOf(
                navArgument("number") { type = NavType.LongType },
                navArgument("allDexNumber") { type = NavType.StringType },
            ),
            enterTransition = {
                slideInHorizontally(animationSpec = spring(stiffness = Spring.StiffnessMedium))
            },
            exitTransition = { fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            NewDetailScreen(routeAction = routAction)
        }
        customComposable(
            route = RouteAction.Download
        ) {
            DownloadScreen(routeAction = routAction)
        }
    }

}

fun getEnterTransition() =
    fadeIn() + slideInVertically(animationSpec = spring(stiffness = Spring.StiffnessMedium))

fun getExitTransition() =
    fadeOut() + slideOutVertically(animationSpec = spring(stiffness = Spring.StiffnessMedium))

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

    fun navToUpdateSearch(type: String) {
        navController.navigate("$UpdateSearch/${type}")
    }

    fun navToUpdateEvolution(index: String) {
        navController.navigate("$UpdateEvolution/$index")
    }

    fun navToNewPokemonDex(index: String) {
        navController.navigate("$NewPokemonDex/$index")
    }

    fun navToArceusDex() {
        navController.navigate(ArcusDex)
    }

    fun navToArceusDetail(
        number: Long,
        allDexNumber: String,
        needPopupBackStack: Boolean = false
    ) {
        navController.navigate("$ArcusDetail/$number/$allDexNumber") {
            if (needPopupBackStack) {
                popupBackStack()
            }
        }
    }

    fun navToArceusDetailBefore(number: Long, allDexNumber: String) {
        navController.navigate("$ArcusDetail/before/$number/$allDexNumber") {
            popupBackStack()
        }
    }

    fun navToDownload() {
        navController.navigate(Download)
    }

    fun popupBackStack() {
        navController.popBackStack()
    }

    companion object {
        const val Home = "home"
        const val Detail = "detail"
        const val Add = "add"
        const val AddEvolution = "add_evolution"
        const val UpdateEvolution = "update_evolution"
        const val UpdateSearch = "update_search"
        const val NewPokemonDex = "new_pokemon_dex"
        const val ArcusDex = "arceus_dex"
        const val ArcusDetail = "arceus_dex_detail"
        const val Download = "download"
        const val List = "List"
        const val Type = "type"
    }

}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.customComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    enterTransition: (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?)? = {
        getEnterTransition()
    },
    exitTransition: (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?)? = {
        getExitTransition()
    },
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        content = content
    )
}